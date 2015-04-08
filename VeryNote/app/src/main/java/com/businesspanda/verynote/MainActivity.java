package com.businesspanda.verynote;

import java.lang.Thread;
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
import jp.kshoji.javax.sound.midi.UsbMidiSystem;

public class MainActivity extends ActionBarActivity  {

    Thread pitch_detector_thread_;

    //public ArrayList<String> allNotes = new ArrayList();
    public String allNotes = "";

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

    UsbMidiSystem usbMidiSystem;

    RelativeLayout linLayout;
    MyTouchListener heyListen;

    public ExportXML exp;
    public PlayMIDI mid;

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
        mid = new PlayMIDI();

        Config.context = this;

        yValueSearch.createYValues();

        RelativeLayout upperLayout = (RelativeLayout) findViewById(R.id.upperLayout);
        heyListen = new MyTouchListener(upperLayout);

        scrollView = (LockableScrollView) findViewById(R.id.scrollview);

        linLayout = new RelativeLayout(this);
        scrollView.addView(linLayout);
        //scrollView.setScrollingEnabled(false);

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

        if (usbMidiSystem != null) {
//            usbMidiSystem.terminate();
        }


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


        changeFreq.setText(nearestNote.name); //remember to remove

        nowTime = System.nanoTime();

        dur = (nowTime - lastNote) / 1000000;

      //  System.out.println(dur + "   durrrrrrrrr " + fullBar/16 + "  " + fullBar*2);

        if(dur>(fullBar*2))lastNote = System.nanoTime();



        if (nearestNote == prevNote){
            noteLength();

        }else if(dur>(fullBar/16)){
            lastNote = System.nanoTime();

            String arrayNote = nearestNote.getName().replaceAll("s","#");

            //allNotes.add(arrayNote);
            allNotes = allNotes + " " + arrayNote;


           /* for(int i = 0; i < allNotes.size();i++) {
                System.out.println("All the notes  " + allNotes.get(i).getName());
            }*/

            notesOnScreen(nearestNote);
            prevNote = nearestNote;

        }

    }

    long durationOfPause;
    long lastPauseWritten;
    long firstPause;

    ImageView pauseImg;

    public void writePause(){
        if(linLayMoving) {
            FrameLayout.LayoutParams pauseParams = new FrameLayout.LayoutParams(
                    FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.pauseWidth)),
                    FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.pauseHeight)));
            firstPause = System.nanoTime();
            durationOfPause = (firstPause - lastNote) / 1000000; //change last note to last pasue written
            System.out.println(durationOfPause + "  pause duration");
            if (durationOfPause < fullBar / 4) {
                pauseImg = new ImageView(this);
                pauseImg.setLayoutParams(pauseParams);
                pauseImg.setX(linLayout.getLayoutParams().width -
                        FitToScreen.returnViewWidth(getPercent(R.dimen.noteStartPos)));
                linLayout.addView(pauseImg);
            }
            if (durationOfPause > fullBar / 2 && durationOfPause < fullBar) {
                pauseImg.setBackgroundColor(Color.BLACK);
                pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseYHalfRest)));
            } else if (durationOfPause > fullBar) {
                pauseImg.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.pauseYWholeRest)));
                 lastPauseWritten = System.nanoTime();

            }
        }
    }


    public void notesOutOfBoundsLines(int nmbOfLines, int nmbOfBassLines, int height){
        FrameLayout.LayoutParams notelineParams= new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.noteLineWidth)),
                ViewGroup.LayoutParams.WRAP_CONTENT);

        int NoteImgX = linLayout.getLayoutParams().width -
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteStartPos) );
        int addToX = FitToScreen.returnViewWidth(getPercent(R.dimen.noteLineOffset));
        int x = NoteImgX + addToX;
      //  System.out.println(NoteImgX + "  <--noteX + addToX--> " + addToX + " x--> " + x);

        if(bass) {
            for (int i = 0; i < nmbOfBassLines; i++) {
                ImageView lineForNote = new ImageView(this);

                lineForNote.setAdjustViewBounds(true);

                lineForNote.setLayoutParams(notelineParams);
                lineForNote.setImageResource(R.drawable.goodline);
                lineForNote.setX(x);
                if (i == 0)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYMiddleC)));
                if (i == 1)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmI)));
                if (i == 2)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmII)));
                if (i == 3)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIII)));
                if (i == 4)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIV)));
                if (i == 5)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmV)));
                if (i>5)System.out.println("need moar lines BASS");
                linLayout.addView(lineForNote);
            }
        }else if(height<26){
            for (int i = 0; i < nmbOfLines; i++) {
                ImageView lineForNote = new ImageView(this);

                lineForNote.setAdjustViewBounds(true);

                lineForNote.setLayoutParams(notelineParams);
                lineForNote.setImageResource(R.drawable.goodline);
                lineForNote.setX(x);
                if (i == 0)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVI)));
                if (i == 1)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVII)));
                if (i == 2)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmVIII)));
                if (i == 3)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmIX)));
                if (i == 4)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmX)));
                if (i == 5)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmXI)));
                if (i == 6)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmXII)));
                if (i == 7)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmXIII)));
                if (i == 8)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYmXIV)));
                if (i>8)System.out.println("need moar lines -----");
                linLayout.addView(lineForNote);
            }
        }else if(height>26){
            for (int i = 0; i < nmbOfLines; i++) {
                ImageView lineForNote = new ImageView(this);

                lineForNote.setAdjustViewBounds(true);

                lineForNote.setLayoutParams(notelineParams);
                lineForNote.setImageResource(R.drawable.goodline);
                lineForNote.setX(x);
                if (i == 0)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYMiddleC)));
                if (i == 1)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpI)));
                if (i == 2)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpII)));
                if (i == 3)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpIII)));
                if (i == 4)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpIV)));
                if (i == 5)
                    lineForNote.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.noteLineYpV)));
                if (i>5)System.out.println("need moar lines ++12");
                linLayout.addView(lineForNote);
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

        allNotes = allNotes + " |";
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

/* //writes string of pitch values to file
   public void writeToFile(String stringarray) {

       File file = new File(Environment.getExternalStorageDirectory(),"FileWriter.txt");

       if(file.exists()) {
           file.delete();
       }

       FileWriter fr = null;
       try {
           fr = new FileWriter(file);
           fr.write(stringarray);
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           //close resources
           try {
               fr.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   }
*/

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

boolean bass = false;
int fullBar = metronomNmb*4; //4 = tempo;
    int addToY = 0;

    public void noteLength(){

        int height = nearestNote.getNoteHeight();
        boolean upSideDown = false;
        addToY = 0;
        if(bass){
            if(height <= 39){
                upSideDown=true;
                addToY = FitToScreen.returnViewHeight(getPercent(R.dimen.upSideDownNoteX));
            }
        }else{
            if(height <= 10) {
                upSideDown = true;
                addToY = FitToScreen.returnViewHeight(getPercent(R.dimen.upSideDownNoteX));
            }

        }

        if(dur >= (fullBar/16) && dur < (fullBar*3/32)) {               //= 1/16 of fullBar
            if (upSideDown) {
                currentNote.setImageResource(R.drawable.upsidedowndoubletailnote);
            } else{
                currentNote.setImageResource(R.drawable.doubletailnote);
            }

        }else if(dur >= (fullBar*3/32) && dur < (fullBar/8)){        //= 3/32 of fullBar
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedowndoubletailnotewdot);
            }else {
                currentNote.setImageResource(R.drawable.doubletailnotewdot);
            }

        }else if(dur >= (fullBar/8) && dur < (fullBar*3/16)){           //= 1/8 of fullBar
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownsingeltailnote);
            }else {
                currentNote.setImageResource(R.drawable.singeltailnaote);
            }

        }else if(dur >= (fullBar*3/16) && dur < (fullBar/4)){        //= 3/16 of fullBar
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownsingletaieotewdot);
            }else {
                currentNote.setImageResource(R.drawable.singeltailnaotewdot);
            }

        }else if(dur >= (fullBar/4) && dur < (fullBar*3/8)) {           //= 1/4 of fullBar
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnote);
            }else {
                currentNote.setImageResource(R.drawable.note);
            }

        }else if(dur >= (fullBar*3/8) && dur < (fullBar/2)) {         //= 3/8 of fullBar
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownnotewdot);
            }else {
                currentNote.setImageResource(R.drawable.notewdot);
            }

        }else if(dur >= (fullBar/2) && dur < (fullBar*3/4)){            //= 2/4 of
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownhollownote);
            }else {
                currentNote.setImageResource(R.drawable.holownote);
            }

        }else if(dur >= (fullBar*3/4) && dur < (fullBar)){          //= 3/4 of fullBar
            if(upSideDown){
                currentNote.setImageResource(R.drawable.upsidedownhollownotewdot);
            }else {
                currentNote.setImageResource(R.drawable.holownotewdot);
            }

        }else if(dur >= (fullBar) && dur < (fullBar*1.5)){              //= 4/4 of fullBar
            currentNote.setImageResource(R.drawable.notailhollownote);

        }else if(dur >= (fullBar*1.5) && dur < fullBar*2){          //= 1 1/2 of fullBar
            currentNote.setImageResource(R.drawable.notailhollownotewdot);

        }else if(dur >= fullBar*2){              // randomly chosen...
            prevNote = new Note(false, false, 0, " ",0 ,0, 0);
        }

    }

    RelativeLayout imgLayout;

    public void notesOnScreen(Note note){

        if(!linLayMoving){
            linLayHandler.postDelayed(moveLinLay, 1);
            firstTempoLine();
            linLayMoving = true;
        }


        imgLayout = new RelativeLayout(this);

        FrameLayout.LayoutParams par = new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteImgWidth)),
                FitToScreen.returnViewHeight(getPercent(R.dimen.noteImgHeight)));

        imgLayout.setLayoutParams(par);
        imgLayout.setFocusable(true);

        currentNote = new ImageView(this);

        noteLength();

        int x = linLayout.getLayoutParams().width -
                FitToScreen.returnViewWidth(getPercent(R.dimen.noteStartPos));

        String notename = note.getName();
        int yID = this.getResources().getIdentifier(notename, "dimen", getPackageName());
        float y = FitToScreen.returnViewHeight(getPercent(yID));

        imgLayout.setX(x);
        imgLayout.setY(y + addToY);

        int noteID = this.getResources().getIdentifier(notename, "id", getPackageName());
        currentNote.setId(noteID);

        notesOutOfBoundsLines(nearestNote.getNmbOfLinesTreble(), nearestNote.getNmbOfLinesBass(), nearestNote.getNoteHeight());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FitToScreen.returnViewHeight(getPercent(R.dimen.noteHeight)));
        currentNote.setLayoutParams(params);
        currentNote.setX(FitToScreen.returnViewWidth(getPercent(R.dimen.noteX)));
        currentNote.setY(0);
        //image.setMaxHeight((int) this.getResources().getDimension(R.dimen.maxHeight));
        //image.setMaxWidth((int) this.getResources().getDimension(R.dimen.maxWidth));
        //currentNote.setImageResource(R.drawable.doubletailnote);

//        currentNote.setImageResource(R.drawable.notailhollownotewdot);

        if(note.sharp){
            ImageView sharp = new ImageView(this);
            sharp.setId(R.id.sharp);
            FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FitToScreen.returnViewHeight(getPercent(R.dimen.sharpHeight)));
            sharp.setLayoutParams(para);

           // sharp.setX((int) this.getResources().getDimension(R.dimen.sharpOffsetX));
           sharp.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.sharpOffsetY)));

            sharp.setImageResource(R.drawable.sharpnotenew);
            imgLayout.addView(sharp);
        }else if(note.flat){
            ImageView flat = new ImageView(this);
            flat.setId(R.id.flat);
            FrameLayout.LayoutParams paraFlat = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FitToScreen.returnViewHeight(getPercent(R.dimen.flatHeight)));
            flat.setLayoutParams(paraFlat);

            //flat.setX((int) this.getResources().getDimension(R.dimen.flatOffsetX));
            flat.setY(FitToScreen.returnViewHeight(getPercent(R.dimen.flatOffsetY)));

            flat.setImageResource(R.drawable.flatnotenew);
            imgLayout.addView(flat);
        }

       // currentNote.setBackgroundColor(getResources().getColor(R.color.yellow));

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
                } else {
                    item.setIcon(R.drawable.ic_action_mic);
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    pitch_detector_thread_.interrupt();
                    runFFT = false;
                    System.out.println("FALSE!!!" + pitch_detector_thread_.isInterrupted());
//                    pitch_detector_thread_.destroy();
                    //mHandler.removeCallbacks(mVibrations);
                    tempolineHandler.removeCallbacks(writeTempoline);
                    tempoStop = System.nanoTime();
                    linLayHandler.removeCallbacks(moveLinLay);
                    linLayout.clearAnimation();
                    linLayout.animate().x(0).setDuration(10);
                    currentLinLayWidth = linLayout.getWidth();
                    xScroll += x;
                    int scrollWidth = scrollView.getWidth();
                    scrollView.scrollTo(scrollWidth - xScroll, 0);
                    x = 0;
                    scrollView.setScrollingEnabled(true);
                    prevNote = new Note(false, false, 0, " ", 0, 0, 0);
                    linLayMoving = false;
                    recording = false;
                    editable = true;
                }
                return true;

            case R.id.action_play:
                    playSound();
                return true;

            case R.id.action_new:
                EditText titleField = (EditText) findViewById(R.id.title_field);
                titleField.setText("Untitled");
                exp.setFilename("untitled");

                allNotes = "";

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

                return true;

            case R.id.action_playmidi:
                Pattern patternMIDI = new Pattern(allNotes);
                mid.playMIDI(patternMIDI);
                return true;


            case R.id.action_save:
                Pattern patternSD = new Pattern(allNotes);
                exp.saveToSD(patternSD);
                return true;
            case R.id.action_share:
                //System.out.println(allNotes);
                Pattern pattern = new Pattern(allNotes);
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
                    }});

                Button btnBass = (Button)popupView.findViewById(R.id.bass);
                btnBass.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clefText.setText("Bass");
                    }
                });


                Button btnCancel = (Button)popupView.findViewById(R.id.cancel);
                btnCancel.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
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