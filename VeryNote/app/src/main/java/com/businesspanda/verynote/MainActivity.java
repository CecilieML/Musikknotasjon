package com.businesspanda.verynote;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    Thread pitch_detector_thread_;
    Thread met_thread;
    public Note prevNote = new Note(false, false, 0, 0, " ");
    public ArrayList<Note> noteArray = new ArrayList();
    public ArrayList<Note> allNotes = new ArrayList();

    public int met_int = 1;

    public boolean playing = false;
    public boolean recording = false;

    public Switch metSwitch;

    private Handler mHandler = new Handler();
    private Handler tempolineHandler = new Handler();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NoteSearch.createTable();
        genTone();
        //keeps screen on
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //lets screen turn off again
        //getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //fitToScreen();

        RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.upperLayout);
        ImageView image = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) this.getResources().getDimension(R.dimen.trebleWidth),
                (int) this.getResources().getDimension(R.dimen.trebleHeight));
        image.setLayoutParams(params);
        image.setX((int) this.getResources().getDimension(R.dimen.trebleX));
        image.setY((int) this.getResources().getDimension(R.dimen.trebleY));
        image.setBackgroundResource(R.drawable.treblebackround);
        theLayout.addView(image);

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
        super.onStop();
        pitch_detector_thread_.interrupt();
        mHandler.removeCallbacks(mVibrations);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public String toString() {
        return "MainActivity{" +
                "noteArray=" + noteArray +
                '}';
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

        //System.out.println(nearestNote + "  nearest note :)");

        changeFreq.setText(nearestNote.name);

        newTime = System.nanoTime();

        dur = (newTime - lastTime)/1000000;

        if((!nearestNote.name.equals(prevNote.name)) && dur>300){

            lastTime = System.nanoTime();

            allNotes.add(nearestNote);

           /* for(int i = 0; i < allNotes.size();i++) {
                System.out.println("All the notes  " + allNotes.get(i).getName());
            }*/

            if(noteArray.size() == 14){
                noteArray.remove(0);
                noteArray.trimToSize();
            }

            noteArray.add(nearestNote);

            notesOnScreen(nearestNote);

            prevNote = nearestNote;

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

        int pos = ((int) this.getResources().getDimension(R.dimen.endPos)+ xOffset);
        image.setX((int) this.getResources().getDimension(R.dimen.noteX) + xOffset);
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
        int x = (int) this.getResources().getDimension(R.dimen.noteX);

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
            sharp.animate().x(pos - xOffset).setInterpolator(interpolator).setDuration(5500);
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
            flat.animate().x(pos - xOffset).setInterpolator(interpolator).setDuration(5500);
            theLayout.addView(flat);
        }

        image.animate().x(pos).setInterpolator(interpolator).setDuration(5500);

        theLayout.addView(image);
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
                    recording = true;
                } else {
                    item.setIcon(R.drawable.ic_action_mic);
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    pitch_detector_thread_.interrupt();
                    mHandler.removeCallbacks(mVibrations);
                    tempolineHandler.removeCallbacks(writeTempoline);
                    recording = false;
                }
                return true;
            case R.id.action_play:
                if(!playing) {
                    item.setIcon(R.drawable.ic_action_pause);
                    playSound();
                    playing = true;
                }else{
                    item.setIcon(R.drawable.ic_action_play);
                    playing = false;
                }
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