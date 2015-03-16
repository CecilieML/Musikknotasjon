package com.businesspanda.verynote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.jfugue.*;

import jp.kshoji.javax.sound.midi.UsbMidiSystem;
import nu.xom.Serializer;


public class MainActivity extends ActionBarActivity  {

    Thread pitch_detector_thread_;

    //public ArrayList<String> allNotes = new ArrayList();
    public String allNotes = "";

    public int met_int = 1;

    public boolean playing = false;
    public boolean recording = false;
    public boolean firstRecording = true;

    public Switch metSwitch;

    private Handler mHandler = new Handler();
    private PauseHandler tempolineHandler = new PauseHandler();
    private Handler linLayHandler = new Handler();

    UsbMidiSystem usbMidiSystem;

    RelativeLayout linLayout;
    MyTouchListener heyListen;

    public ExportXML exp;

    public String title = "Untitled";

    public String metSpeed = "750";

    ImageView currentNote;
    Note prevNote;

    LockableScrollView scrollView;

    ImageView backgroundImage;

    RelativeLayout lowestLayer;
    FrameLayout mainScreen;

    String stringarray;


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
        RelativeLayout.LayoutParams paramsLinLayout = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.lowestLayerWidth),
                (int) this.getResources().getDimension(R.dimen.lowestLayerHeight));
        linLayout.setLayoutParams(paramsLinLayout);

        scrollView.addView(linLayout);

        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

        //RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.backgroundLayer);
       /* backgroundImage = new ImageView(this);

        backgroundImage.setBackgroundColor(getResources().getColor(R.color.cyan));
*/

    /*    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.trebleWidth),
                (int) this.getResources().getDimension(R.dimen.trebleHeight));
       // params.addRule(RelativeLayout.CENTER_IN_PARENT);
        backgroundImage.setLayoutParams(params);

        backgroundImage.setX((int) this.getResources().getDimension(R.dimen.trebleX));
        backgroundImage.setY((int) this.getResources().getDimension(R.dimen.trebleY));

       /* backgroundImage.setImageResource(R.drawable.trebleline);

        theLayout.addView(backgroundImage);*/


        metSwitch = (Switch) findViewById(R.id.metronomeswitch);

        metSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()){
                    mHandler.postDelayed(mVibrations, 750);
                } else{
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

    public void fitToScreen() {
        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);

        float density = getResources().getDisplayMetrics().density;

        //tinyphone = 1.5, other phones = 3.0
        if(density>2){

            frame.setPadding(
                    (int) this.getResources().getDimension(R.dimen.marginLeft),
                    (int) this.getResources().getDimension(R.dimen.marginTop),0,0);

        }

    }

    long lastTime;
    long newTime;
    long dur;

    public void ShowPitchDetectionResult( final double pitch) {

        final TextView changeFreq = (TextView) findViewById(R.id.freqTextview);

        Integer pitchInt = (int) (pitch);
        Note nearestNote = NoteSearch.findNearestNote(pitchInt);

        stringarray = stringarray + " " + pitchInt;


        changeFreq.setText(nearestNote.name);

        newTime = System.nanoTime();

        dur = (newTime - lastTime)/1000000;

        if(nearestNote==prevNote)
//            noteLength();

        if(dur>300 && nearestNote!=prevNote){

            lastTime = System.nanoTime();

            String arrayNote = nearestNote.getName().replaceAll("s","#");

            //allNotes.add(arrayNote);
            allNotes = allNotes + " " + arrayNote;


           /* for(int i = 0; i < allNotes.size();i++) {
                System.out.println("All the notes  " + allNotes.get(i).getName());
            }*/



//            notesOnScreen(nearestNote);
            prevNote = nearestNote;

        }

    }

    private Runnable writeTempoline = new Runnable() {
        public void run() {
            tempolineOnScreen(getTempoUpperY());
            tempolineOnScreen(getTempoLowerY());
            allNotes = allNotes + " |";
            tempolineHandler.postDelayed(writeTempoline, 1500);
        }
    };

    public int getTempoUpperY(){
        return  (int) this.getResources().getDimension(R.dimen.upperTempolineY);
    }

    public int getTempoLowerY(){
        return  (int) this.getResources().getDimension(R.dimen.lowerTempolineY);
    }

    public void tempolineOnScreen(int y){
      //  LinearInterpolator interpolator = new LinearInterpolator();
      //  RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.lowestLayer);
        ImageView tempo = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.tempolineWidth),
                (int) this.getResources().getDimension(R.dimen.tempolineHeight));
        tempo.setLayoutParams(params);
        tempo.setBackgroundColor(getResources().getColor(R.color.lineColor));

        //int xOffset = (int) this.getResources().getDimension(R.dimen.tempolineOffsetX);

       // int pos = ((int) this.getResources().getDimension(R.dimen.endPos));
        tempo.setX(linLayout.getLayoutParams().width - (int) this.getResources().getDimension(R.dimen.noteStartPos));
        tempo.setY(y);

        //image.animate().x(pos).setInterpolator(interpolator).setDuration(5500);

        linLayout.addView(tempo);
    }

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
            mHandler.postDelayed(mVibrations, 750);
        }
    };


    public void noteLength(){

        if(dur>=100 && dur<200){
            currentNote.setImageResource(R.drawable.doubletailnote);
        }else if(dur>=200 && dur<400){
            currentNote.setImageResource(R.drawable.singeltailnaote);

        }else if(dur>=400 && dur<600){
            currentNote.setImageResource(R.drawable.note);
        }else if(dur>=600 && dur<1000){
            currentNote.setImageResource(R.drawable.holownote);
        }else if(dur>=1000){
            prevNote = new Note(false, false, 0, 0, " ");
        }

        //image.setImageResourse(R.drawable.doublenote);


    }

    boolean linLayMoving = false;

    public void notesOnScreen(Note note){


        if(!linLayMoving){
            linLayHandler.postDelayed(moveLinLay, 1);
            if(firstRecording){
                tempolineHandler.postDelayed(writeTempoline, 1);
            }else{
                tempolineHandler.resume();
            }

        }
        linLayMoving = true;
        firstRecording = false;

       // LinearInterpolator interpolator = new LinearInterpolator();

        //RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.lowestLayer);

        RelativeLayout imgLayout = new RelativeLayout(this);

        RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.layWidth),
                (int) this.getResources().getDimension(R.dimen.layHeight));
        imgLayout.setLayoutParams(par);

        currentNote = new ImageView(this);

        imgLayout.setFocusable(true);

        //int pos = (int) this.getResources().getDimension(R.dimen.endPos);
        int x = linLayout.getLayoutParams().width - (int) this.getResources().getDimension(R.dimen.noteStartPos);

        String notename = note.getName();
        int yID = this.getResources().getIdentifier(notename, "dimen", getPackageName());
        double y = (int)this.getResources().getDimension(yID);

        imgLayout.setY(FitToScreen.returnViewHeight(y));
        imgLayout.setX(x);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.noteWidth),
                (int) this.getResources().getDimension(R.dimen.noteHeight));
        currentNote.setLayoutParams(params);
        currentNote.setX(this.getResources().getDimension(R.dimen.noteX));
        currentNote.setY(0);
        //image.setMaxHeight((int) this.getResources().getDimension(R.dimen.maxHeight));
        //image.setMaxWidth((int) this.getResources().getDimension(R.dimen.maxWidth));
        currentNote.setImageResource(R.drawable.doubletailnote);

        if(note.sharp){
            ImageView sharp = new ImageView(this);
            RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(
                    (int) this.getResources().getDimension(R.dimen.sharpWidth),
                    (int) this.getResources().getDimension(R.dimen.sharpHeight));
            sharp.setLayoutParams(para);

            sharp.setX((int) this.getResources().getDimension(R.dimen.sharpOffsetX));
            sharp.setY((int) this.getResources().getDimension(R.dimen.sharpOffsetY));

            sharp.setImageResource(R.drawable.sharpnote);
           // sharp.animate().x(pos - xOffset).setInterpolator(interpolator).setDuration(5500);
            imgLayout.addView(sharp);
        }else if(note.flat){
            ImageView flat = new ImageView(this);
            RelativeLayout.LayoutParams paraFlat = new RelativeLayout.LayoutParams(
                    (int) this.getResources().getDimension(R.dimen.flatWidth),
                    (int) this.getResources().getDimension(R.dimen.flatHeight));
            flat.setLayoutParams(paraFlat);

            flat.setX((int) this.getResources().getDimension(R.dimen.flatOffsetX));
            flat.setY((int) this.getResources().getDimension(R.dimen.flatOffsetY));
            flat.setImageResource(R.drawable.flatnote);
         //   flat.animate().x(pos - xOffset).setInterpolator(interpolator).setDuration(5500);
            imgLayout.addView(flat);
        }

       // image.animate().x(pos).setInterpolator(interpolator).setDuration(5500);
        currentNote.setOnTouchListener(heyListen);
        imgLayout.addView(currentNote);
        linLayout.addView(imgLayout);
        if(!recording){
            linLayHandler.removeCallbacks(moveLinLay);
            tempolineHandler.pause();
        }
    }

    private final int duration = 3; // seconds
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
        return (int) this.getResources().getDimension(R.dimen.Offset);
    }

    int x = 0;
    int xScroll = 0;
    int speed = 100;
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


boolean treble = true;


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
                    recording = true;
                } else {
                    item.setIcon(R.drawable.ic_action_mic);
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    pitch_detector_thread_.interrupt();
                    mHandler.removeCallbacks(mVibrations);
                    tempolineHandler.pause();
                    linLayHandler.removeCallbacks(moveLinLay);
                    linLayout.clearAnimation();
                    linLayout.animate().x(0).setDuration(10);
                    xScroll += x;
                    int scrollWidth = scrollView.getWidth();
                    scrollView.scrollTo(scrollWidth - xScroll, 0);
                    x = 0;
                    scrollView.setScrollingEnabled(true);
                    linLayMoving = false;
                    recording = false;
                }
                return true;
            case R.id.action_play:
                if(!playing) {
                    item.setIcon(R.drawable.ic_action_pause);
                    playSound();
                    playing = true;

                    writeToFile(stringarray);


                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                    /*offsetY = displayMetrics.heightPixels - mainScreen.getMeasuredHeight();

                    System.out.println(offsetY + " <---- offsetY(heightpixel-measueredheigh)");
                    System.out.println(displayMetrics.heightPixels + " <---- heightpixels");
                    System.out.println(lowestLayer.getMeasuredHeight() + " <---- measuredheight");*/


                }else{
                    item.setIcon(R.drawable.ic_action_play);
                    playing = false;
                }
                return true;



            case R.id.trebleBass:
                if(treble){
                    item.setIcon(R.drawable.bass);
                    backgroundImage.setImageResource(R.drawable.trebleline);
                    treble = false;
                }else{
                    item.setIcon(R.drawable.treble);
                    backgroundImage.setImageResource(R.drawable.bassline);
                    treble = true;
                }
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

                Button btnCancel = (Button)popupView.findViewById(R.id.cancel);
                btnCancel.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }});

                popupWindow.showAtLocation(this.findViewById(R.id.metronomeswitch), Gravity.CENTER, 0, 0);
                popupWindow.setFocusable(true);

                EditText met_speed_field = (EditText) popupView.findViewById(R.id.speedtext);

                //met_speed_field.setText("www");

                met_speed_field.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        EditText speedfield = (EditText) popupView.findViewById(R.id.speedtext);
                        metSpeed = speedfield.getText().toString();
                        System.out.println("metspeed ----->  " + metSpeed);
                        return false;
                    }
                });


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