package com.businesspanda.verynote;

/** Copyright (C) 2015 by BusinessPanda - Cecilie M. Langfeldt, Helene H. Larsen.
 **
 ** Permission to use, copy, modify, and distribute this software and its
 ** documentation for any purpose and without fee is hereby granted, provided
 ** that the above copyright notice appear in all copies and that both that
 ** copyright notice and this permission notice appear in supporting
 ** documentation.  This software is provided "as is" without express or
 ** implied warranty.
 */

import java.lang.Thread;
import java.util.ArrayList;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import org.jfugue.*;


public class MainActivity extends ActionBarActivity  {

    Thread pitch_detector_thread_;

    MyTouchListener heyListen;
    RestListener restListener;

    LockableScrollView scrollView;

    Note copyNote;
    Note prevNote;

    ExportXML exp;

    Switch metSwitch;

    String title = "Untitled";

    ArrayList<Note> allNotesForXML = new ArrayList();
    ArrayList<String> currentMeasure = new ArrayList();

    private Handler mHandler = new Handler();
    private Handler linLayHandler = new Handler();
    private Handler tempolineHandler = new Handler();

    ImageView flat;
    ImageView sharp;
    ImageView neutral;
    ImageView pauseImg;
    ImageView currentNote;
    ImageView backgroundImage;

    RelativeLayout imgLayout;
    RelativeLayout linLayout;
    RelativeLayout lowestLayer;

    int x = 0;
    int speed = 70;
    int addToY = 0;
    int met_int = 1;
    int xScroll = 0;
    int linLayStartX;
    int metronomNmb = 750;
    int currentLinLayWidth;
    int noteIdxInXMLArray = 0;
    int fullBar = metronomNmb*4; //4 = tempo;
    final int numSamplesForGenA4 = 8000; //duration 1 second * sampleRate 8000

    long dur;
    long nowTime;
    long lastNote;
    long tempoStop;
    long firstPause;
    long durationOfPause;
    long lastPauseWritten;
    long lastTempolineWasWritten = 0;

    boolean onlyOnce;
    boolean linLayMoving;
    boolean startNewNote;
    boolean setHalfRestX;
    boolean brandNewPiece;
    boolean setSharpAndSuch;
    boolean addWholeRestToList;
    boolean useLastPauseWritten;

    boolean bass = false;
    boolean recording = false;
    boolean clefChanged = false;

    public static boolean editable;
    public static boolean runFFT = true;

    final byte generatedA4Snd[] = new byte[2 * numSamplesForGenA4];
    final double sampleForGenA4[] = new double[numSamplesForGenA4];


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NoteSearch.createTable();
        genTone();

        lowestLayer = (RelativeLayout) findViewById(R.id.lowestLayer);

        onlyOnce = true;

        exp = new ExportXML();

        Config.context = this;

        yValueSearch.createYValues();

        RelativeLayout upperLayout = (RelativeLayout) findViewById(R.id.upperLayout);
        heyListen = new MyTouchListener(upperLayout, allNotesForXML);
        restListener = new RestListener(upperLayout, allNotesForXML);

        scrollView = (LockableScrollView) findViewById(R.id.scrollview);

        linLayout = new RelativeLayout(this);
        scrollView.addView(linLayout);

        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

        metSwitch = (Switch) findViewById(R.id.metronomeswitch);
        metSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    mHandler.postDelayed(mVibrations, metronomNmb);
                } else {
                    met_int = 1;
                    TextView text = (TextView) findViewById(R.id.met_text);
                    text.setText(" ");
                    mHandler.removeCallbacks(mVibrations);
                }
            }
        });

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //Add the custom view to the action bar
        actionBar.setCustomView(R.layout.edit_text);
        EditText edit_title = (EditText) actionBar.getCustomView().findViewById(R.id.title_field);
        edit_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                EditText titleField = (EditText) findViewById(R.id.title_field);
                title = titleField.getText().toString();
                //Removes spaces from title
                exp.setFilename(title.replaceAll(" ", ""));
                return false;
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE);
    }

    @Override
    //Called when main screen receives or loses focus
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            FrameLayout.LayoutParams paramsLinLayout = new FrameLayout.LayoutParams(
                    FitToScreen.returnViewWidth(getPercent(R.dimen.linLayoutStartWidth)),
                    FitToScreen.returnViewHeight(getPercent(R.dimen.lowestLayerHeight)));
            linLayout.setLayoutParams(paramsLinLayout);

            if(!onlyOnce && !brandNewPiece && !clefChanged) {
                linLayout.getLayoutParams().width = currentLinLayWidth;
            }

            if(onlyOnce){
                linLayout.setX(FitToScreen.returnViewWidth(getPercent(R.dimen.linLayoutStartX)));
                linLayStartX = FitToScreen.returnViewWidth(getPercent(R.dimen.linLayoutStartX));
                onlyOnce = false;
            }

            RelativeLayout.LayoutParams backgroundParams = new RelativeLayout.LayoutParams(
                    FitToScreen.returnViewWidth(getPercent(R.dimen.backgroundWidth)),
                    FitToScreen.returnViewHeight(getPercent(R.dimen.backgroundHeight)));
            backgroundImage.setLayoutParams(backgroundParams);
            backgroundImage.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.backgroundTrebleY)));
            if(bass)backgroundImage.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.backgroundBassY)));

            backgroundImage.setAdjustViewBounds(true);

            FrameLayout.LayoutParams lowestLayerParams = new FrameLayout.LayoutParams(
                    FitToScreen.returnViewWidth(getPercent(R.dimen.lowestLayerWidth)),
                    FitToScreen.returnViewHeight(getPercent(R.dimen.lowestLayerHeight)));
            lowestLayerParams.gravity = Gravity.CENTER;
            lowestLayer.setLayoutParams(lowestLayerParams);

            linLayStartX = (int)linLayout.getX();

            brandNewPiece = false;
            clefChanged = false;
        }else{
            stopRecording();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        tempolineHandler.removeCallbacks(writeTempoline);
        mHandler.removeCallbacks(mVibrations);
        runFFT = false;
        try {
            super.onStop();
            pitch_detector_thread_.interrupt();
        }catch (Exception e){

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    // Takes in id of percent value, and returns that percent value
    public static float getPercent(int id){
        TypedValue outValue = new TypedValue();
        Config.context.getResources().getValue(id, outValue, true);
        float returnValue = outValue.getFloat();
        return returnValue;
    }

    // Called form PitchDec, calls notesOnScreen or noteLength
    public void ShowPitchDetectionResult(final double pitch) {
        Integer pitchInt = (int) (pitch);
        Note nearestNote = NoteSearch.findNearestNote(pitchInt);

        lastPauseWritten = System.nanoTime();
        nowTime = System.nanoTime();
        dur = (nowTime - lastNote) / 1000000;

        if (nearestNote == prevNote && !startNewNote) {
            noteLength(copyNote, currentNote);
        }else {
            lastNote = System.nanoTime();
            startNewNote = false;
            useLastPauseWritten = false;
            copyNote = notesOnScreen(nearestNote);
            prevNote = nearestNote;
        }
    }

    // Called from PitchDec, writes pause to screen
    public void writePause() {
        if (linLayMoving) {
            startNewNote = true;
            keepLatestNote();

            FrameLayout.LayoutParams pauseParams = new FrameLayout.LayoutParams(
                    FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.pauseWidth)),
                    FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.pauseHeight)));
            firstPause = System.nanoTime();

            if (!useLastPauseWritten) {
                durationOfPause = (firstPause - lastNote) / 1000000;
                useLastPauseWritten = true;
            } else {
                durationOfPause = (firstPause - lastPauseWritten) / 1000000;
            }

            lastNote = System.nanoTime();

            if (durationOfPause < fullBar / 4) {
                pauseImg = new ImageView(this);
                pauseImg.setLayoutParams(pauseParams);
                pauseImg.setAdjustViewBounds(true);
                linLayout.addView(pauseImg);
                setHalfRestX = true;
                addWholeRestToList = true;
            }

            if (durationOfPause > fullBar / 2 && durationOfPause < fullBar) {
                if (pauseImg != null) {
                    pauseImg.setBackgroundColor(Color.BLACK);
                    if (bass) {
                        pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseBassYHalfRest)));
                    } else {
                        pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseYHalfRest)));
                    }
                    if (setHalfRestX) {
                        pauseImg.setX(linLayout.getLayoutParams().width -
                                FitToScreen.returnViewWidth(getPercent(R.dimen.pauseXHalfRest)));
                        Note pauseNote = new Note(false, false, false, 0, "R", 0, 0, 0, "h");
                        allNotesForXML.add(pauseNote);
                        pauseImg.setTag(noteIdxInXMLArray);
                        noteIdxInXMLArray++;
                        setHalfRestX = false;
                        pauseImg.setOnTouchListener(restListener);
                    }
                }
            } else if (durationOfPause > fullBar) {

                if (pauseImg != null) {
                    if (bass) {
                        pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseBassYWholeRest)));
                    } else {
                        pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseYWholeRest)));
                    }
                    if (addWholeRestToList) {
                        pauseImg.setX(linLayout.getLayoutParams().width -
                                FitToScreen.returnViewWidth(getPercent(R.dimen.pauseXWholeRest)));

                        findLastPause:
                        for (int i = 1; i < allNotesForXML.size(); i++) {
                            if (allNotesForXML.get(allNotesForXML.size() - i).getName().equals("R")) {
                                allNotesForXML.get(allNotesForXML.size() - i).setDurationOfNote("w");
                                break findLastPause;
                            }
                        }

                        addWholeRestToList = false;
                    }
                    lastPauseWritten = System.nanoTime();
                }
            }
        }
    }

    // Creates ImageViews for the notes helplines
    public void notesOutOfBoundsLines(int nmbOfLines, int nmbOfBassLines, int height, RelativeLayout imgLayout){
        FrameLayout.LayoutParams notelineParams= new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.noteLineWidth)),
                ViewGroup.LayoutParams.WRAP_CONTENT);

        int x = FitToScreen.returnViewWidth(getPercent(R.dimen.noteLineOffset));

        if(bass) {
            for (int i = 0; i < nmbOfBassLines; i++) {
                ImageView lineForNote = new ImageView(this);
                lineForNote.setAdjustViewBounds(true);
                lineForNote.setLayoutParams(notelineParams);
                lineForNote.setImageResource(R.drawable.goodline);
                lineForNote.setX(x);
                if (i == 0) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYMiddleC)));
                    lineForNote.setId(R.id.notelineI);
                }
                if (i == 1) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmI)));
                    lineForNote.setId(R.id.notelineII);
                }
                if (i == 2) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmII)));
                    lineForNote.setId(R.id.notelineIII);
                }
                if (i == 3) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIII)));
                    lineForNote.setId(R.id.notelineIV);
                }
                if (i == 4) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIV)));
                    lineForNote.setId(R.id.notelineV);
                }
                if (i == 5) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmV)));
                    lineForNote.setId(R.id.notelineVI);
                }
                if (i == 6) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVI)));
                    lineForNote.setId(R.id.notelineVII);
                }
                if (i == 7) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVII)));
                    lineForNote.setId(R.id.notelineVIII);
                }
                if (i == 8) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVIII)));
                    lineForNote.setId(R.id.notelineIX);
                }
                if (i == 9) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIX)));
                    lineForNote.setId(R.id.notelineX);
                }
                if (i == 10) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmX)));
                    lineForNote.setId(R.id.notelineXI);
                }
                imgLayout.addView(lineForNote);
            }
        }else if(height<26){
            for (int i = 0; i < nmbOfLines; i++) {

                ImageView lineForNote = new ImageView(this);
                lineForNote.setAdjustViewBounds(true);
                lineForNote.setLayoutParams(notelineParams);
                lineForNote.setImageResource(R.drawable.goodline);
                lineForNote.setX(x);
                if (i == 0) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVI)));
                    lineForNote.setId(R.id.notelineI);
                }
                if (i == 1) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVII)));
                    lineForNote.setId(R.id.notelineII);
                }
                if (i == 2) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVIII)));
                    lineForNote.setId(R.id.notelineIII);
                }
                if (i == 3) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIX)));
                    lineForNote.setId(R.id.notelineIV);
                }
                if (i == 4) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmX)));
                    lineForNote.setId(R.id.notelineV);
                }
                imgLayout.addView(lineForNote);
            }
        }else if(height>26){
            for (int i = 0; i < nmbOfLines; i++) {
                ImageView lineForNote = new ImageView(this);
                lineForNote.setAdjustViewBounds(true);
                lineForNote.setLayoutParams(notelineParams);
                lineForNote.setImageResource(R.drawable.goodline);
                lineForNote.setX(x);
                if (i == 0) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYMiddleC)));
                    lineForNote.setId(R.id.notelineI);
                }
                if (i == 1) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpI)));
                    lineForNote.setId(R.id.notelineII);
                }
                if (i == 2) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpII)));
                    lineForNote.setId(R.id.notelineIII);
                }
                if (i == 3) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpIII)));
                    lineForNote.setId(R.id.notelineIV);
                }
                imgLayout.addView(lineForNote);
            }
        }
    }

    // Called with handler, wries tempo lines to screen
    public void tempolineOnScreen(){
        ImageView tempo = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(getPercent(R.dimen.tempolineWidth)),
                FitToScreen.returnViewHeight(getPercent(R.dimen.tempolineHeight)));
        tempo.setLayoutParams(params);
        tempo.setBackgroundColor(getResources().getColor(R.color.lineColor));

        tempo.setX(linLayout.getLayoutParams().width -
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteStartPos)) +
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteImgWidth)));

        tempo.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.tempolineY)));
        if(bass)tempo.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.bassTempolineY)));

        Note tempolineNote = new Note(false, false, false, 0, "|", 0, 0, 0, "q");

        allNotesForXML.add(tempolineNote);
        noteIdxInXMLArray++;

        synchronized (this) {
            linLayout.addView(tempo);
            currentMeasure.clear();
            lastTempolineWasWritten = System.nanoTime();
        }

    }

    // Called from notesOnScreen, writes first tempo line and starts tempolineHandler
    public void firstTempoLine() {
        long usedTime = (tempoStop - lastTempolineWasWritten) / 1000000;
        long writeAt = fullBar - usedTime;
        tempolineHandler.postDelayed(writeTempoline, writeAt);
    }

    // Determines what picture to use for note and if helplines and markings should be added to screen yet
    public void noteLength(Note nearestNote, ImageView currentNote) {
        int height = nearestNote.getNoteHeight();
        boolean upSideDown = false;
        addToY = 0;
        if (bass) {
            if (height <= 44) {
                upSideDown = true;
                imgLayout.setId(R.id.upsideDown);
                addToY = FitToScreen.returnViewHeight(getPercent(R.dimen.upSideDownNoteX));
            }
        } else {
            if (height <= 23) {
                upSideDown = true;
                imgLayout.setId(R.id.upsideDown);
                addToY = FitToScreen.returnViewHeight(getPercent(R.dimen.upSideDownNoteX));
            }
        }

        if(dur >= (fullBar/16) && setSharpAndSuch){

            if(markNote(nearestNote, currentNote))sharpFlat(nearestNote);
            currentMeasure.add(nearestNote.getName());

            notesOutOfBoundsLines(nearestNote.getNmbOfLinesTreble(), nearestNote.getNmbOfLinesBass(),
                    nearestNote.getNoteHeight(), imgLayout);
            currentNote.setOnTouchListener(heyListen);

            setSharpAndSuch = false;
        }

        if(dur < (fullBar/16)){
            if (upSideDown) {
                currentNote.setImageResource(R.drawable.upsidedownmarknote);
            } else{
                currentNote.setImageResource(R.drawable.marknote);
            }
        }else if(dur >= (fullBar/16) && dur < (fullBar*3/32)) {      //= 1/16 of fullBar
           nearestNote.setDurationOfNote("s");
            if (upSideDown) {
                currentNote.setImageResource(R.drawable.upsidedowndoubletailnote);
            } else{
                currentNote.setImageResource(R.drawable.doubletailnote);
            }

        }else if(dur >= (fullBar*3/32) && dur < (fullBar/8)){        //= 3/32 of fullBar
            nearestNote.setDurationOfNote("s.");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedowndoubletailnotewdot);
            }else {
                currentNote.setImageResource(R.drawable.doubletailnotewdot);
            }

        }else if(dur >= (fullBar/8) && dur < (fullBar*3/16)){        //= 1/8 of fullBar
            nearestNote.setDurationOfNote("i");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownsingeltailnote);
            }else {
                currentNote.setImageResource(R.drawable.singeltailnaote);
            }

        }else if(dur >= (fullBar*3/16) && dur < (fullBar/4)){        //= 3/16 of fullBar
            nearestNote.setDurationOfNote("i.");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownsingletaieotewdot);
            }else {
                currentNote.setImageResource(R.drawable.singeltailnaotewdot);
            }

        }else if(dur >= (fullBar/4) && dur < (fullBar*3/8)) {        //= 1/4 of fullBar
            nearestNote.setDurationOfNote("q");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnote);
            }else {
                currentNote.setImageResource(R.drawable.note);
            }

        }else if(dur >= (fullBar*3/8) && dur < (fullBar/2)) {        //= 3/8 of fullBar
            nearestNote.setDurationOfNote("q.");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnotewdot);
            }else {
                currentNote.setImageResource(R.drawable.notewdot);
            }

        }else if(dur >= (fullBar/2) && dur < (fullBar*3/4)){         //= 2/4 of fullBar
            nearestNote.setDurationOfNote("h");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownhollownote);
            }else {
                currentNote.setImageResource(R.drawable.holownote);
            }

        }else if(dur >= (fullBar*3/4) && dur < (fullBar)){           //= 3/4 of fullBar
            nearestNote.setDurationOfNote("h.");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownhollownotewdot);
            }else {
                currentNote.setImageResource(R.drawable.holownotewdot);
            }

        }else if(dur >= (fullBar) && dur < (fullBar*1.5)){           //= 4/4 of fullBar
            nearestNote.setDurationOfNote("w");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnotailhollownote);
            }else {
                currentNote.setImageResource(R.drawable.notailhollownote);
            }

        }else if(dur >= (fullBar*1.5) && dur < fullBar*1.6){         //= 1 1/2 of fullBar
            nearestNote.setDurationOfNote("w.");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnotailhollownotewdot);
            }else {
                currentNote.setImageResource(R.drawable.notailhollownotewdot);
            }

        }else if(dur >= fullBar*1.6){
            prevNote = new Note(false, false, false, 0, " ",0 ,0, 0, "x");
        }

    }

    // Checks if latest note was long enough to keep, if not it is discarded
    public boolean keepLatestNote(){
        if(currentNote==null){
            return false;
        }else {
            if(currentNote.getDrawable().getConstantState() == MainActivity.this
                    .getResources().getDrawable(R.drawable.marknote).getConstantState() ||
                     currentNote.getDrawable().getConstantState() == MainActivity.this
                    .getResources().getDrawable(R.drawable.upsidedownmarknote).getConstantState()){

                currentNote.setImageResource(R.drawable.emptynote);
            }
            return true;
        }
    }

    // Creates ImageViews for the notes marking
    public void sharpFlat(Note note) {
        String noteName = note.getName();
        //int yID = this.getResources().getIdentifier(noteName, "dimen", getPackageName());
        float noteY;
        if(noteName.substring(1, 2).equals("n")) {
            String nameFix = noteName.substring(0, 1) + noteName.substring(2, 3);
            int yID = this.getResources().getIdentifier(nameFix, "dimen", getPackageName());
            noteY = FitToScreen.returnViewHeight(getPercent(yID));
        }else {
            int yID = this.getResources().getIdentifier(noteName, "dimen", getPackageName());
            noteY = FitToScreen.returnViewHeight(getPercent(yID));
        }

        if (note.sharp) {
            sharp = new ImageView(this);
            sharp.setId(R.id.sharp);
            FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FitToScreen.returnViewHeight(getPercent(R.dimen.sharpHeight)));
            sharp.setLayoutParams(para);

            sharp.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.sharpOffsetY)) + noteY);

            sharp.setImageResource(R.drawable.sharpnotenew);
            imgLayout.addView(sharp);

        } else if (note.flat) {
            flat = new ImageView(this);
            flat.setId(R.id.flat);
            FrameLayout.LayoutParams paraFlat = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FitToScreen.returnViewHeight(getPercent(R.dimen.flatHeight)));
            flat.setLayoutParams(paraFlat);

            flat.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.flatOffsetY))+ noteY);

            flat.setImageResource(R.drawable.flatnotenew);
            imgLayout.addView(flat);

        } else if (note.neutral) {
            neutral = new ImageView(this);
            neutral.setId(R.id.neutral);
            FrameLayout.LayoutParams paraNeutral = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FitToScreen.returnViewHeight(getPercent(R.dimen.sharpHeight)));
            neutral.setLayoutParams(paraNeutral);

            neutral.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.sharpOffsetY))+ noteY);

            neutral.setImageResource(R.drawable.naturalnote);
            imgLayout.addView(neutral);
        }
    }

    // Determins if note should be marked or not
    public boolean markNote(Note note, ImageView noteImg){
        String fullName = note.getName();

        String root = fullName.substring(0, 1);
        String octave = fullName.substring(1, 2);
        String nowMark = null;
        if(fullName.length() == 3){
            nowMark = fullName.substring(1, 2);
            octave = fullName.substring(2, 3);
        }

        String lastMark = null;
        for(int i = 0; i<currentMeasure.size(); i++){
            if(root.equals(currentMeasure.get(i).substring(0, 1))) {
                if (currentMeasure.get(i).length() == 3) {
                    if(currentMeasure.get(i).substring(2, 3).equals(octave)) {
                        lastMark = currentMeasure.get(i).substring(1, 2);
                    }
                }
            }
        }

        if(lastMark!=null){
            if(nowMark == null) {
                if(!lastMark.equals("n")){
                    note.setNeutral(true);
                    String neutralName = root + "n" + octave;
                    note.setName(neutralName);
                    int noteID = this.getResources().getIdentifier(neutralName, "dimen", getPackageName());
                    if (noteID != 0) {
                        noteImg.setId(noteID);
                    } else {
                        try {
                            noteID = R.id.class.getField(root + octave).getInt(null);
                        } catch (NoSuchFieldException e) {

                        } catch (IllegalAccessException f) {

                        }
                        noteImg.setId(noteID);
                    }
                    return true;
                }
            }else {
                 if (nowMark.equals(lastMark)) {
                     return false;
                 } else {
                     return true;
                 }

            }
        }
        return true;
    }

    // Creates ImageView and RelativeLayout for note
    public Note notesOnScreen(Note inNote){
        if(!linLayMoving){
            linLayHandler.postDelayed(moveLinLay, 1);
            firstTempoLine();
            linLayMoving = true;
        }

        keepLatestNote();

        imgLayout = new RelativeLayout(this);
        currentNote = new ImageView(this);

        Note note = null;
        try{
           note = (Note) inNote.clone();
       }catch (CloneNotSupportedException e){

       }

        currentNote.setTag(noteIdxInXMLArray);
        noteIdxInXMLArray++;
        allNotesForXML.add(note);

        FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteImgWidth)),
                FitToScreen.returnViewHeight(getPercent(R.dimen.noteImgFULLHeight)));
        imgLayout.setLayoutParams(par);
        imgLayout.setFocusable(true);

        int x = linLayout.getLayoutParams().width -
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteStartPos));
        imgLayout.setX(x);

        String notename = note.getName();
        int yID = this.getResources().getIdentifier(notename, "dimen", getPackageName());
        float y;
        if(yID != 0) {
            y = FitToScreen.returnViewHeight(getPercent(yID));
        } else {
            try {
                yID = R.id.class.getField(notename).getInt(null);
            } catch (NoSuchFieldException e) {

            } catch (IllegalAccessException f) {

            }
            y = FitToScreen.returnViewHeight(getPercent(yID));
        }

        int noteID = this.getResources().getIdentifier(notename, "id", getPackageName());
        currentNote.setId(noteID);

        dur = 0;
        setSharpAndSuch = true;
        noteLength(note, currentNote);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FitToScreen.returnViewHeight(getPercent(R.dimen.noteHeight)));
        currentNote.setLayoutParams(params);
        currentNote.setX(FitToScreen.returnViewWidth(getPercent(R.dimen.noteX)));
        currentNote.setY(y + addToY);

        imgLayout.addView(currentNote);
        if(runFFT)linLayout.addView(imgLayout);

        if(!recording){
            linLayHandler.removeCallbacks(moveLinLay);
            tempolineHandler.removeCallbacks(writeTempoline);
        }
        return note;
    }

    // Generates tone of A4 (samplerate 8000/frequency of A4 440hz)
    void genTone(){
        for (int i = 0; i < numSamplesForGenA4; ++i) {
            sampleForGenA4[i] = Math.sin(2 * Math.PI * i / (8000/440));
        }
        int idx = 0;
        for (final double dVal : sampleForGenA4) {
            final short val = (short) ((dVal * 32767));
            generatedA4Snd[idx++] = (byte) (val & 0x00ff);
            generatedA4Snd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    //Plays generated A4 sound from genTone().
    void playSound(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                8000, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedA4Snd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedA4Snd, 0, generatedA4Snd.length);
        audioTrack.play();

    }

    public void stopRecording(){
        android.support.v7.internal.view.menu.ActionMenuItemView recBtn =(android.support.v7.
                internal.view.menu.ActionMenuItemView) findViewById(R.id.action_record);
        recBtn.setIcon(getResources().getDrawable(R.drawable.ic_action_mic));
        recBtn.setTitle(getResources().getString(R.string.record));
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        try {
            pitch_detector_thread_.interrupt();
        }catch(NullPointerException e){

        }

        runFFT = false;
        tempolineHandler.removeCallbacks(writeTempoline);
        tempoStop = System.nanoTime();

        keepLatestNote();

        linLayHandler.removeCallbacks(moveLinLay);
        if(linLayout.getWidth() < lowestLayer.getWidth()) {
            linLayStartX = (int) linLayout.getX();
        }else{
            linLayStartX = 0;
        }

        linLayout.clearAnimation();
        linLayout.animate().x(linLayStartX).setDuration(10);
        currentLinLayWidth = linLayout.getWidth();
        xScroll += x;
        int scrollWidth = scrollView.getWidth();
        scrollView.scrollTo(scrollWidth - xScroll, 0);
        x = 0;
        scrollView.setScrollingEnabled(true);
        prevNote = new Note(false, false, false, 0, " ", 0, 0, 0, "");
        linLayMoving = false;
        recording = false;
        editable = true;
    }

    public void resetAll(){
        EditText titleField = (EditText) findViewById(R.id.title_field);
        titleField.setText("Untitled");
        exp.setFilename("untitled");
        if(brandNewPiece)metronomNmb = 750;

        allNotesForXML.clear();

        linLayout.getLayoutParams().width = FitToScreen.returnViewWidth(getPercent(R.dimen.lowestLayerWidth));

        scrollView.removeView(linLayout);
        linLayout = new RelativeLayout(this);
        scrollView.addView(linLayout);

        linLayout.getLayoutParams().width = FitToScreen.returnViewWidth(getPercent(R.dimen.linLayoutStartWidth));
        linLayout.setX(FitToScreen.returnViewWidth(getPercent(R.dimen.linLayoutStartX)));
        linLayStartX = (int)linLayout.getX();

        try {
            heyListen.removeButtons();
        }catch(NullPointerException e){

        }

        try {
            restListener.removeButton();
        }catch(NullPointerException e){

        }


        noteIdxInXMLArray = 0;

    }

    // Returns offset used to animate linLayout
    public int Offset(){
        return FitToScreen.returnViewWidth(getPercent(R.dimen.Offset));
    }

    // Animates linLayout
    private Runnable moveLinLay = new Runnable() {
        public void run() {
            LinearInterpolator interpolator = new LinearInterpolator();
            linLayout.animate().x(linLayStartX+x).setInterpolator(interpolator).setDuration(speed);
            x -= Offset();
            linLayout.getLayoutParams().width += Offset();
            linLayout.requestLayout();
            linLayHandler.postDelayed(moveLinLay, speed);
        }
    };

    //  Metronome, changes number and vibrates
    private Runnable mVibrations = new Runnable() {
        public void run() {
            TextView text = (TextView) findViewById(R.id.met_text);

            String str_met = Integer.toString(met_int);
            text.setText(str_met);
            met_int++;
            if(met_int>4)met_int=1;

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(50);
            mHandler.postDelayed(mVibrations, metronomNmb);
        }
    };

    // Runnable that calls tempolineOnScreen
    private Runnable writeTempoline = new Runnable() {
        public void run() {
            tempolineOnScreen();
            tempolineHandler.postDelayed(writeTempoline, fullBar);
        }
    };

    @Override
    // Handles presses on the action bar items
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_record:
                if(!recording) {
                    item.setIcon(R.drawable.ic_action_stop);
                    item.setTitle(R.string.stop);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    pitch_detector_thread_ = new Thread(new PitchDec(this, new Handler()));
                    pitch_detector_thread_.start();
                    int scrollWidth = scrollView.getWidth();
                    scrollView.scrollTo(scrollWidth - xScroll, 0);
                    scrollView.setScrollingEnabled(false);
                    linLayMoving = false;
                    recording = true;
                    runFFT = true;
                    editable = false;
                    lastNote = System.nanoTime();
                    useLastPauseWritten = false;
                } else {
                    stopRecording();

                }
                return true;

            case R.id.action_play:
                playSound();
                return true;

            case R.id.action_new:
                brandNewPiece = true;
                resetAll();
                return true;

            case R.id.action_save:
                Pattern patternSD = new Pattern(exp.convertArrayListToString(allNotesForXML));
                exp.saveToSD(patternSD);
                return true;

            case R.id.action_share:
                Pattern pattern = new Pattern(exp.convertArrayListToString(allNotesForXML));
                exp.saveToFile(pattern);
                exp.sendToEmail();
                return true;

            case R.id.action_settings:


                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popupView = layoutInflater.inflate(R.layout.settings_popup, null);

                final PopupWindow popupWindow = new PopupWindow(
                popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

                popupWindow.showAtLocation(this.findViewById(R.id.metronomeswitch), Gravity.CENTER, 0, 0);
                popupWindow.setFocusable(true);

                final SeekBar seekBar = (SeekBar)popupView.findViewById(R.id.speedBar);
                final TextView speedText = (TextView)popupView.findViewById(R.id.speedValue);
                final TextView clefText = (TextView)popupView.findViewById(R.id.clef_text);
                final TextView warningText = (TextView)popupView.findViewById(R.id.warning);

                if (bass) {
                    clefText.setText("Bass");
                }  else {
                    clefText.setText("Treble");
                }

                speedText.setText(String.valueOf((60000/metronomNmb)));
                seekBar.setProgress((60000/metronomNmb)-60);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        speedText.setText(String.valueOf(progress+60));

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                Button btnTreble = (Button)popupView.findViewById(R.id.treble);
                btnTreble.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        clefText.setText("Treble");
                        if(bass) {
                            clefChanged = true;
                            warningText.setText(getResources().getString(R.string.warning_text));
                        }
                    }});

                Button btnBass = (Button)popupView.findViewById(R.id.bass);
                btnBass.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clefText.setText("Bass");
                        if(!bass) {
                            clefChanged = true;
                            warningText.setText(getResources().getString(R.string.warning_text));
                        }
                    }
                });


                Button btnCancel = (Button)popupView.findViewById(R.id.cancel);
                btnCancel.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        clefChanged = false;
                    }});

                Button btnOk = (Button)popupView.findViewById(R.id.ok);
                btnOk.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        metronomNmb = 60000/(Integer.parseInt(String.valueOf(speedText.getText())));
                        fullBar = metronomNmb*4;

                        if(clefText.getText() == "Treble") {
                            backgroundImage.setImageResource(R.drawable.trebleline);
                            backgroundImage.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.backgroundTrebleY)));
                            bass = false;
                        } else {
                            backgroundImage.setImageResource(R.drawable.bassline);
                            backgroundImage.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.backgroundBassY)));
                            bass = true;
                        }

                        if(clefChanged) {
                            resetAll();
                        }

                        popupWindow.dismiss();
                    }});

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    // Inflate the menu items for use in the action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}