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
import android.util.Log;
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

    public ArrayList<Note> allNotesForXML = new ArrayList();
    //public String allNotes = ""; <-gammel string for xml

    public int met_int = 1;

    public boolean playing = false;
    public boolean recording = false;
    public boolean firstRecording = true;
    boolean linLayMoving; //= false;
    public static boolean editable;

    public Switch metSwitch;

    private Handler mHandler = new Handler();
    private Handler tempolineHandler = new Handler();
    private Handler linLayHandler = new Handler();


    RelativeLayout linLayout;
    MyTouchListener heyListen;

    public ExportXML exp;

    public String title = "Untitled";

    ImageView currentNote;
    Note prevNote;
    Note nearestNote;

    LockableScrollView scrollView;

    ImageView backgroundImage;

    RelativeLayout lowestLayer;
    //FrameLayout mainScreen;

    String stringarray;

    int metronomNmb = 750;
    public int currentLinLayWidth;

    boolean bass = false;
    int fullBar = metronomNmb*4; //4 = tempo;
    int addToY = 0;
    int addToSharpFlat = 0;

    boolean clefChanged = false;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NoteSearch.createTable();
        genTone();

        lowestLayer = (RelativeLayout) findViewById(R.id.lowestLayer);

        //fitToScreen();

        exp = new ExportXML();

        Config.context = this;

        yValueSearch.createYValues();

        RelativeLayout upperLayout = (RelativeLayout) findViewById(R.id.upperLayout);
        heyListen = new MyTouchListener(upperLayout);

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
        // add the custom view to the action bar
        actionBar.setCustomView(R.layout.edit_text);
        EditText edit_title = (EditText) actionBar.getCustomView().findViewById(R.id.title_field);
        edit_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                EditText titleField = (EditText) findViewById(R.id.title_field);
                title = titleField.getText().toString();
                //removes spaces from title
                exp.setFilename(title.replaceAll(" ", ""));
                return false;
            }
        });
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_TITLE);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){

        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            FrameLayout.LayoutParams paramsLinLayout = new FrameLayout.LayoutParams(
                    FitToScreen.returnViewWidth(getPercent(R.dimen.lowestLayerWidth)),
                    FitToScreen.returnViewHeight(getPercent(R.dimen.lowestLayerHeight)));
            linLayout.setLayoutParams(paramsLinLayout);

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

            if(currentLinLayWidth>FitToScreen.returnViewWidth(getPercent(R.dimen.lowestLayerWidth))){
                linLayout.getLayoutParams().width = currentLinLayWidth;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //pitch_detector_thread_ = new Thread(new PitchDec(this, new Handler()));
        //pitch_detector_thread_.start();
        //met_thread = new Thread(new Metronome(findViewById(R.id.metronomeswitch)));
        //met_thread.start();

    }

    @Override
    public void onStop() {
        tempolineHandler.removeCallbacks(writeTempoline);
        runFFT = false;
        try {
            super.onStop();
            pitch_detector_thread_.interrupt();
        }catch (Exception e){
            Log.e("null pointer exception", "Didn't use pitch_detector");
        }

        mHandler.removeCallbacks(mVibrations);
        //playHandler.removeCallbacks(playSoundLoop);


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }



    public static float getPercent(int id){
        TypedValue outValue = new TypedValue();
        Config.context.getResources().getValue(id, outValue, true);
        float returnValue = outValue.getFloat();
        return returnValue;
    }



   /* public static float getScrollViewTop(){
        //int[] XYpos = new int[2];
        return scrollY;
    }*/

    long lastNote;
    long nowTime;
    long dur;

    public void ShowPitchDetectionResult( final double pitch) {

        final TextView changeFreq = (TextView) findViewById(R.id.freqTextview);

        Integer pitchInt = (int) (pitch);
        nearestNote = NoteSearch.findNearestNote(pitchInt);

        stringarray = stringarray + " " + pitchInt;

        //SOMETHING OR OTHER TO FIX PAUSES
        lastPauseWritten = System.nanoTime();
        /***/

        changeFreq.setText(nearestNote.name); //remember to remove

        nowTime = System.nanoTime();

        dur = (nowTime - lastNote) / 1000000;

        if (nearestNote == prevNote && !newNote) {
            noteLength();

        }else if(dur>(fullBar/16)){
            newNote = false;
            lastNote = System.nanoTime();
            useLastPauseWritten = false;

            /***/
            allNotesForXML.add(nearestNote);
            /***/

            notesOnScreen(nearestNote);
            prevNote = nearestNote;
        }

    }

    long durationOfPause;
    long lastPauseWritten;
    long firstPause;

    ImageView pauseImg;

    boolean useLastPauseWritten;
    boolean setHalfRestX;
    boolean addWholeRestToList;
    boolean newNote;

    public void writePause(){
        if(linLayMoving) {
            newNote = true;
            FrameLayout.LayoutParams pauseParams = new FrameLayout.LayoutParams(
                    FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.pauseWidth)),
                    FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.pauseHeight)));
            firstPause = System.nanoTime();

            if (!useLastPauseWritten) {
                durationOfPause = (firstPause - lastNote) / 1000000; //change last note to last pasue written
                useLastPauseWritten = true;
            } else {
                durationOfPause = (firstPause - lastPauseWritten) / 1000000; //change last note to last pasue written
            }

            lastNote = System.nanoTime();

            if (durationOfPause < fullBar / 4) {
                pauseImg = new ImageView(this);
                pauseImg.setLayoutParams(pauseParams);
                linLayout.addView(pauseImg);
               setHalfRestX = true;
            }

            if (durationOfPause > fullBar / 2 && durationOfPause < fullBar) {
                if(pauseImg!=null) {
                    pauseImg.setBackgroundColor(Color.BLACK);
                    if(bass) {
                        pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseBassYHalfRest)));
                    }else{
                        pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseYHalfRest)));
                    }
                    if(setHalfRestX){
                        pauseImg.setX(linLayout.getLayoutParams().width -
                                FitToScreen.returnViewWidth(getPercent(R.dimen.pauseXHalfRest)));
                        Note pauseNote = new Note(false, false, 0, "R", 0, 0, 0, "h");
                        allNotesForXML.add(pauseNote);
                        setHalfRestX = false;
                    }
                }
            } else if (durationOfPause > fullBar) {
                Note pauseNote = new Note(false, false, 0, "R", 0, 0, 0, "w");
                allNotesForXML.add(pauseNote);
                if(pauseImg!=null) {
                    if(bass) {
                        pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseBassYWholeRest)));
                    }else{
                        pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseYWholeRest)));
                    }
                    if(addWholeRestToList){
                        pauseImg.setX(linLayout.getLayoutParams().width -
                                FitToScreen.returnViewWidth(getPercent(R.dimen.pauseXWholeRest)));
                        allNotesForXML.add(pauseNote);
                        addWholeRestToList = false;
                    }
                    lastPauseWritten = System.nanoTime();

                }


            }
        }
    }

    public void notesOutOfBoundsLines(int nmbOfLines, int nmbOfBassLines, int height){
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
                    lineForNote.setId(R.id.lineI);
                }
                if (i == 1) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmI)));
                    lineForNote.setId(R.id.lineII);
                }
                if (i == 2) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmII)));
                    lineForNote.setId(R.id.lineIII);
                }
                if (i == 3) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIII)));
                    lineForNote.setId(R.id.lineIV);
                }
                if (i == 4) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIV)));
                    lineForNote.setId(R.id.lineV);
                }
                if (i == 5) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmV)));
                    lineForNote.setId(R.id.lineVI);
                }
                if (i == 6) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVI)));
                    lineForNote.setId(R.id.lineVII);
                }
                if (i == 7) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVII)));
                    lineForNote.setId(R.id.lineVIII);
                }
                if (i == 8) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVIII)));
                    lineForNote.setId(R.id.lineIX);
                }
                if (i == 9) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIX)));
                    lineForNote.setId(R.id.lineX);
                }
                if (i == 10) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmX)));
                    lineForNote.setId(R.id.lineXI);
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
                    lineForNote.setId(R.id.lineI);
                }
                if (i == 1) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVII)));
                    lineForNote.setId(R.id.lineII);
                }
                if (i == 2) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVIII)));
                    lineForNote.setId(R.id.lineIII);
                }
                if (i == 3) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIX)));
                    lineForNote.setId(R.id.lineIV);
                }
                if (i == 4) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmX)));
                    lineForNote.setId(R.id.lineV);
                }
                if (i == 5) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmXI)));
                    lineForNote.setId(R.id.lineVI);
                }
                if (i == 6) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmXII)));
                    lineForNote.setId(R.id.lineVII);
                }
                if (i == 7) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmXIII)));
                    lineForNote.setId(R.id.lineVIII);
                }
                if (i == 8) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmXIV)));
                    lineForNote.setId(R.id.lineIX);
                }
                if (i>8)System.out.println("need moar lines -----");
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
                    lineForNote.setId(R.id.lineI);
                }
                if (i == 1) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpI)));
                    lineForNote.setId(R.id.lineII);
                }
                if (i == 2) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpII)));
                    lineForNote.setId(R.id.lineIII);
                }
                if (i == 3) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpIII)));
                    lineForNote.setId(R.id.lineIV);
                }
                if (i == 4) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpIV)));
                    lineForNote.setId(R.id.lineV);
                }
                if (i == 5) {
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpV)));
                    lineForNote.setId(R.id.lineVI);
                }
                if (i>5)System.out.println("need moar lines ++12");
                imgLayout.addView(lineForNote);
            }
        }
    }

    private Runnable writeTempoline = new Runnable() {
        public void run() {
            tempolineOnScreen();
            tempolineHandler.postDelayed(writeTempoline, fullBar);
        }
    };

    public void tempolineOnScreen(){
        ImageView tempo = new ImageView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(getPercent(R.dimen.tempolineWidth)),
                FitToScreen.returnViewHeight(getPercent(R.dimen.tempolineHeight)));
        tempo.setLayoutParams(params);
        tempo.setBackgroundColor(getResources().getColor(R.color.lineColor));

        tempo.setX(linLayout.getLayoutParams().width -
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteStartPos)));
        tempo.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.tempolineY)));
        if(bass)tempo.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.bassTempolineY)));

        //allNotes = allNotes + " |"; <--gammel tempolinje for lagring xml

        Note tempolineNote = new Note(false, false, 0, "|", 0, 0, 0, "");

        allNotesForXML.add(tempolineNote);

        linLayout.addView(tempo);
        lastTempolineWasWritten = System.nanoTime();

    }

    long lastTempolineWasWritten = 0;
    long tempoStop;

    public void firstTempoLine() {
        long usedTime = (tempoStop - lastTempolineWasWritten) / 1000000;
        long writeAt = fullBar - usedTime;

        //System.out.println(writeAt + " writeAt,   first tempo line,   usedTime " + usedTime);

        tempolineHandler.postDelayed(writeTempoline, writeAt);
    }

    private Runnable mVibrations = new Runnable() {
        public void run() {
            TextView text = (TextView) findViewById(R.id.met_text);

            String str_met = Integer.toString(met_int);
            text.setText(str_met);
            met_int++;
            if(met_int>4)met_int=1;

            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            //Vibrate for 50 milliseconds
            v.vibrate(50);
            //Wait for 750 ms
            mHandler.postDelayed(mVibrations, metronomNmb);
        }
    };

    public void noteLength(){

        int height = nearestNote.getNoteHeight();
        boolean upSideDown = false;
        addToY = 0;
        addToSharpFlat = 0;
        if(bass){
            if(height <= 37){
                upSideDown=true;
                currentNote.setId(R.id.upsideDown);
                addToY = FitToScreen.returnViewHeight(getPercent(R.dimen.upSideDownNoteX));
            }
        }else{
            if(height <= 20) {
                upSideDown = true;
                currentNote.setId(R.id.upsideDown);
                addToY = FitToScreen.returnViewHeight(getPercent(R.dimen.upSideDownNoteX));
            }

        }

        if(dur >= (fullBar/16))sharpFlat(nearestNote);

        if(dur >= (fullBar/16) && dur < (fullBar*3/32)) {               //= 1/16 of fullBar
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

        }else if(dur >= (fullBar/8) && dur < (fullBar*3/16)){           //= 1/8 of fullBar
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

        }else if(dur >= (fullBar/4) && dur < (fullBar*3/8)) {           //= 1/4 of fullBar
            nearestNote.setDurationOfNote("q");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnote);
            }else {
                currentNote.setImageResource(R.drawable.note);
            }

        }else if(dur >= (fullBar*3/8) && dur < (fullBar/2)) {         //= 3/8 of fullBar
            nearestNote.setDurationOfNote("q.");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnotewdot);
            }else {
                currentNote.setImageResource(R.drawable.notewdot);
            }

        }else if(dur >= (fullBar/2) && dur < (fullBar*3/4)){            //= 2/4 of
            nearestNote.setDurationOfNote("h");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownhollownote);
            }else {
                currentNote.setImageResource(R.drawable.holownote);
            }

        }else if(dur >= (fullBar*3/4) && dur < (fullBar)){          //= 3/4 of fullBar
            nearestNote.setDurationOfNote("h.");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownhollownotewdot);
            }else {
                currentNote.setImageResource(R.drawable.holownotewdot);
            }

        }else if(dur >= (fullBar) && dur < (fullBar*1.5)){              //= 4/4 of fullBar
            nearestNote.setDurationOfNote("w");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnotailhollownote);
            }else {
                currentNote.setImageResource(R.drawable.notailhollownote);
            }

        }else if(dur >= (fullBar*1.5) && dur < fullBar*1.6){          //= 1 1/2 of fullBar
            nearestNote.setDurationOfNote("w.");
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnotailhollownotewdot);
            }else {
                currentNote.setImageResource(R.drawable.notailhollownotewdot);
            }

        }else if(dur >= fullBar*1.6){              // randomly chosen...
            prevNote = new Note(false, false, 0, " ",0 ,0, 0, "");
        }

    }

    RelativeLayout imgLayout;
    ImageView sharp;
    ImageView flat;

    public void sharpFlat(Note note) {

        String noteName = note.getName();
        int yID = this.getResources().getIdentifier(noteName, "dimen", getPackageName());
        float noteY = FitToScreen.returnViewHeight(getPercent(yID));

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
        }
    }

    public void notesOnScreen(Note note){

        if(!linLayMoving){
            linLayHandler.postDelayed(moveLinLay, 1);
            firstTempoLine();
            linLayMoving = true;
        }

        imgLayout = new RelativeLayout(this);
        currentNote = new ImageView(this);

        /*FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteImgWidth)),
                FitToScreen.returnViewHeight(getPercent(R.dimen.noteImgHeight))); this works (kinda)*/

        FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteImgWidth)),
                FitToScreen.returnViewHeight(getPercent(R.dimen.noteImgFULLHeight)));

        imgLayout.setLayoutParams(par);
        imgLayout.setFocusable(true);

        noteLength();

        int x = linLayout.getLayoutParams().width -
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteStartPos));

        String notename = note.getName();
        int yID = this.getResources().getIdentifier(notename, "dimen", getPackageName());
        float y = FitToScreen.returnViewHeight(getPercent(yID));

        imgLayout.setX(x);
      //  imgLayout.setY(y + addToY);

        int noteID = this.getResources().getIdentifier(notename, "id", getPackageName());
        currentNote.setId(noteID);

        notesOutOfBoundsLines(nearestNote.getNmbOfLinesTreble(), nearestNote.getNmbOfLinesBass(), nearestNote.getNoteHeight());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FitToScreen.returnViewHeight(getPercent(R.dimen.noteHeight)));
        currentNote.setLayoutParams(params);
        currentNote.setX(FitToScreen.returnViewWidth(getPercent(R.dimen.noteX)));
        currentNote.setY(y + addToY);

        currentNote.setOnTouchListener(heyListen);
        imgLayout.addView(currentNote);
        linLayout.addView(imgLayout);

        if(!recording){
            linLayHandler.removeCallbacks(moveLinLay);
            tempolineHandler.removeCallbacks(writeTempoline);
        }

    }

    private final int duration = 1; // seconds
    private final int sampleRate = 8000;
    private final int numSamples = duration * sampleRate;
    private final double sample[] = new double[numSamples];
    private final double freqOfTone = 440; // hz

    private final byte generatedSnd[] = new byte[2 * numSamples];

    void genTone(){
        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }





    void playSound(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();

    }

    public int Offset(){
        return FitToScreen.returnViewWidth(getPercent(R.dimen.Offset));
    }

    int x = 0;
    int xScroll = 0;
    int speed = 70;
    private Runnable moveLinLay = new Runnable() {
        public void run() {
            LinearInterpolator interpolator = new LinearInterpolator();
            linLayout.animate().x(x).setInterpolator(interpolator).setDuration(speed);
            x -= Offset();
            linLayout.getLayoutParams().width += Offset();
            linLayout.requestLayout();
            linLayHandler.postDelayed(moveLinLay, speed);
        }
    };

    public static boolean runFFT = true;

    public void resetAll(){

        EditText titleField = (EditText) findViewById(R.id.title_field);
        titleField.setText("Untitled");
        exp.setFilename("untitled");

        //allNotes = ""; <--gammel sletting av string for xml
        allNotesForXML.clear();

        linLayout.getLayoutParams().width = FitToScreen.returnViewWidth(getPercent(R.dimen.lowestLayerWidth));

        TextView noteView = (TextView) findViewById(R.id.freqTextview);
        noteView.setText("");

        if(recording) {
            android.support.v7.internal.view.menu.ActionMenuItemView recBtn =(android.support.v7.internal.view.menu.ActionMenuItemView) findViewById(R.id.action_record);
            recBtn.setIcon(getResources().getDrawable(R.drawable.ic_action_mic));
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            pitch_detector_thread_.interrupt();
            tempolineHandler.removeCallbacks(writeTempoline);
            linLayHandler.removeCallbacks(moveLinLay);
            recording = false;
        }

        scrollView.removeView(linLayout);
        linLayout = new RelativeLayout(this);
        scrollView.addView(linLayout);

        //missing stuff..

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_record:
                if(!recording) {
                    item.setIcon(R.drawable.ic_action_stop);
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
                    item.setIcon(R.drawable.ic_action_mic);
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    pitch_detector_thread_.interrupt();
                    runFFT = false;
                    tempolineHandler.removeCallbacks(writeTempoline);
                    tempoStop = System.nanoTime();
                    linLayHandler.removeCallbacks(moveLinLay);
                    linLayout.getLayoutParams().width += 3*Offset();
                    linLayout.clearAnimation();
                    linLayout.animate().x(0).setDuration(10);
                    currentLinLayWidth = linLayout.getWidth();
                    xScroll += x;
                    int scrollWidth = scrollView.getWidth();
                    scrollView.scrollTo(scrollWidth - xScroll, 0);
                    x = 0;
                    scrollView.setScrollingEnabled(true);
                    prevNote = new Note(false, false, 0, " ", 0, 0, 0, "");
                    linLayMoving = false;
                    recording = false;
                    editable = true;
                }
                return true;

            case R.id.action_play:
                playSound();
                return true;

            case R.id.action_new:
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
                            clefChanged = false;
                        }

                        popupWindow.dismiss();
                    }});

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}