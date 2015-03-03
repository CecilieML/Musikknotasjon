package com.businesspanda.verynote;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;
import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.text.method.Touch;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewAnimator;

import org.jfugue.*;

import jp.kshoji.javax.sound.midi.UsbMidiSystem;


public class MainActivity extends ActionBarActivity  {

    Thread pitch_detector_thread_;

    public ArrayList<Note> allNotes = new ArrayList();

    public int met_int = 1;

    public boolean playing = false;
    public boolean recording = false;

    public Switch metSwitch;

    private Handler mHandler = new Handler();
    private Handler tempolineHandler = new Handler();
    private Handler linLayHandler = new Handler();

    UsbMidiSystem usbMidiSystem;

    RelativeLayout linLayout;
    MyTouchListener heyListen;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NoteSearch.createTable();
        genTone();

        //fitToScreen();


        Config.context = this;

        RelativeLayout really = (RelativeLayout) findViewById(R.id.middleLayer);
        heyListen = new MyTouchListener(really);

        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.scrollview);

        linLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams paramsLinLayout = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.lowestLayerWidth),
                (int) this.getResources().getDimension(R.dimen.lowestLayerHeight));
        linLayout.setLayoutParams(paramsLinLayout);

        linLayout.setBackgroundColor(getResources().getColor(R.color.pink));

        //linLayout.setOrientation(LinearLayout.HORIZONTAL);

        scrollView.addView(linLayout);

        RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.middleLayer);
        ImageView image = new ImageView(this);
        ImageView boxLeft = new ImageView(this);
        ImageView boxRight = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.trebleWidth),
                (int) this.getResources().getDimension(R.dimen.trebleHeight));
        image.setLayoutParams(params);

        RelativeLayout.LayoutParams paramsBox = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.boxParamWidth),
                (int) this.getResources().getDimension(R.dimen.boxParamHeight));
        boxLeft.setLayoutParams(paramsBox);
        boxRight.setLayoutParams(paramsBox);

        image.setX((int) this.getResources().getDimension(R.dimen.trebleX));
        image.setY((int) this.getResources().getDimension(R.dimen.trebleY));

        boxLeft.setX((int) this.getResources().getDimension(R.dimen.boxLeftX));
        boxLeft.setY((int) this.getResources().getDimension(R.dimen.boxLeftY));

        boxRight.setX((int) this.getResources().getDimension(R.dimen.boxRightX));
        boxRight.setY((int) this.getResources().getDimension(R.dimen.boxRightY));

        image.setBackgroundResource(R.drawable.treblebackround);
        boxRight.setBackgroundColor(getResources().getColor(R.color.lightPurple));
        boxLeft.setBackgroundColor(getResources().getColor(R.color.lightPurple));

        theLayout.addView(boxLeft);
        theLayout.addView(boxRight);
        theLayout.addView(image);


        /*
        FileOutputStream file = this.openFileOutput("music.xml", MODE_PRIVATE);

        MusicXmlRenderer renderer = new MusicXmlRenderer();
        MusicStringParser parser = new MusicStringParser();
        parser.addParserListener(renderer);

        Pattern pattern = new Pattern("C D E F G A B |");
        parser.parse(pattern);

        Serializer serializer = new Serializer(file, "UTF-8");
        serializer.setIndent(4);
        serializer.write(renderer.getMusicXMLDoc());

        file.flush();
        file.close();
*/


        usbMidiSystem = new UsbMidiSystem(this);
        usbMidiSystem.initialize();

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

        try {
            super.onStop();
            pitch_detector_thread_.interrupt();
        }catch (Exception e){
            Log.e("Didn't use pitch_detector", "null pointer exception");
        }

        mHandler.removeCallbacks(mVibrations);
        //playHandler.removeCallbacks(playSoundLoop);

        if (usbMidiSystem != null) {
            usbMidiSystem.terminate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

  /*  public void fitToScreen() {

        ImageView image = (ImageView)findViewById(R.id.treble);

        float density = getResources().getDisplayMetrics().density;

        //tinyphone = 1.5, other phones = 3.0
        if(density>2){
            image.setPadding(0, 0, 0, 120);
        }

    }*/

    long lastTime;
    long newTime;
    long dur;

    public void ShowPitchDetectionResult( final double pitch) {

        final TextView changeFreq = (TextView) findViewById(R.id.freqTextview);

        Integer pitchInt = (int) (pitch);
        Note nearestNote = NoteSearch.findNearestNote(pitchInt);

        changeFreq.setText(nearestNote.name);

        newTime = System.nanoTime();

        dur = (newTime - lastTime)/1000000;

        if(dur>300){

            lastTime = System.nanoTime();

            allNotes.add(nearestNote);

           /* for(int i = 0; i < allNotes.size();i++) {
                System.out.println("All the notes  " + allNotes.get(i).getName());
            }*/

            notesOnScreen(nearestNote);

        }



    }

    private Runnable writeTempoline = new Runnable() {
        public void run() {
            tempolineOnScreen(getTempoUpperY());
            tempolineOnScreen(getTempoLowerY());
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
        LinearInterpolator interpolator = new LinearInterpolator();
        RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.lowestLayer);
        ImageView image = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.tempolineWidth),
                (int) this.getResources().getDimension(R.dimen.tempolineHeight));
        image.setLayoutParams(params);
        image.setBackgroundColor(0xFF000000);

        int xOffset = (int) this.getResources().getDimension(R.dimen.tempolineOffsetX);

        int pos = ((int) this.getResources().getDimension(R.dimen.endPos));
        image.setX((int) this.getResources().getDimension(R.dimen.startPos));
        image.setY(y);

        image.animate().x(pos).setInterpolator(interpolator).setDuration(5500);

        theLayout.addView(image);
    }

   /* public void writeToFile(){
        //String theentirearraythingstring = test.toString();

        FileWriter fw;
        try {
            File f = this.getFilesDir();
            String s = f.getCanonicalPath();
            File file = new File(s+"/awesomefile.txt");
            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fw = new FileWriter(file);
            fw.write(theentirearraythingstring);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

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



    public void notesOnScreen(Note note){

        LinearInterpolator interpolator = new LinearInterpolator();

        RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.lowestLayer);

        ImageView image = new ImageView(this);

        int pos = (int) this.getResources().getDimension(R.dimen.endPos);
        int x = linLayout.getLayoutParams().width - (int) this.getResources().getDimension(R.dimen.noteX);

                //(int) this.getResources().getDimension(R.dimen.noteX);

        String notename = note.getName();
        int yID = this.getResources().getIdentifier(notename, "dimen", getPackageName());
        int y = (int)this.getResources().getDimension(yID);

       // int y = note.getyValue();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.noteWidth),
                (int) this.getResources().getDimension(R.dimen.noteHeight));
        image.setLayoutParams(params);
        image.setX(x);
        image.setY(y);
        image.setMaxHeight((int) this.getResources().getDimension(R.dimen.maxHeight));
        image.setMaxWidth((int) this.getResources().getDimension(R.dimen.maxWidth));
        image.setBackgroundResource(R.drawable.singlenote);
        if(note.sharp){
            ImageView sharp = new ImageView(this);
            RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(
                    (int) this.getResources().getDimension(R.dimen.sharpWidth),
                    (int) this.getResources().getDimension(R.dimen.sharpHeight));
            sharp.setLayoutParams(para);

            int xOffset = (int) this.getResources().getDimension(R.dimen.sharpOffsetX);
            int yOffset = (int) this.getResources().getDimension(R.dimen.sharpOffsetY);

            sharp.setX(x - xOffset);
            sharp.setY(y + yOffset);
            sharp.setBackgroundResource(R.drawable.sharpnote);
           // sharp.animate().x(pos - xOffset).setInterpolator(interpolator).setDuration(5500);
            theLayout.addView(sharp);
        }else if(note.flat){
            ImageView flat = new ImageView(this);
            RelativeLayout.LayoutParams paraFlat = new RelativeLayout.LayoutParams(
                    (int) this.getResources().getDimension(R.dimen.flatWidth),
                    (int) this.getResources().getDimension(R.dimen.flatHeight));
            flat.setLayoutParams(paraFlat);

            int xOffset = (int) this.getResources().getDimension(R.dimen.flatOffsetX);
            int yOffset = (int) this.getResources().getDimension(R.dimen.flatOffsetY);

            flat.setX(x - xOffset);
            flat.setY(y + yOffset);
            flat.setBackgroundResource(R.drawable.flatnote);
         //   flat.animate().x(pos - xOffset).setInterpolator(interpolator).setDuration(5500);
            theLayout.addView(flat);
        }

       // image.animate().x(pos).setInterpolator(interpolator).setDuration(5500);
        image.setOnTouchListener(heyListen);
                linLayout.addView(image);
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


    void playFancySound() {
        Player player = new Player();
        player.play("C D E F G A B");
    }



/*
    void playSound(){
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();

    }
    */

    public int Offset(){
        return (int) this.getResources().getDimension(R.dimen.Offset);
    }

    int x = 0;
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
                    tempolineHandler.postDelayed(writeTempoline, 1);
                    linLayHandler.postDelayed(moveLinLay, 1);

                    recording = true;
                } else {
                    item.setIcon(R.drawable.ic_action_mic);
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    pitch_detector_thread_.interrupt();
                    mHandler.removeCallbacks(mVibrations);
                    tempolineHandler.removeCallbacks(writeTempoline);
                    linLayHandler.removeCallbacks(moveLinLay);
                    linLayout.clearAnimation();
                    linLayout.animate().x(0).setDuration(10);
                    x = 0;
                    recording = false;
                }
                return true;
            case R.id.action_play:
                if(!playing) {
                    item.setIcon(R.drawable.ic_action_pause);
                    playFancySound();
                    //ExportXML exp = new ExportXML();
                    //exp.export();
                    playing = true;
                }else{
                    item.setIcon(R.drawable.ic_action_play);
                    playing = false;
                }
                return true;
            case R.id.action_share:
                ExportXML exp = new ExportXML();
                exp.sendToEmail();
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