package com.businesspanda.verynote;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread;
import java.util.ArrayList;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
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

    public Switch metSwitch;

    private Handler mHandler = new Handler();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NoteSearch.createTable();


        metSwitch = (Switch) findViewById(R.id.metronomeswitch);


        metSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (buttonView.isChecked()){
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    //Vibrate for 500 milliseconds
                    v.vibrate(500);
                    System.out.println("ON! ");
                } else{
                    System.out.println("OFF!");
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        pitch_detector_thread_ = new Thread(new PitchDec(this, new Handler()));
        pitch_detector_thread_.start();
        met_thread = new Thread(new Metronome(findViewById(R.id.metronomeswitch)));
        met_thread.start();

    }

    @Override
    public void onStop() {
        super.onStop();
        pitch_detector_thread_.interrupt();
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

    public void ShowPitchDetectionResult( final double pitch) {

        final TextView changeFreq = (TextView) findViewById(R.id.freqTextview);

        Integer pitchInt = (int) (pitch);
        Note nearestNote = NoteSearch.findNearestNote(pitchInt);

        //System.out.println(nearestNote + "  nearest note :)");

        changeFreq.setText(nearestNote.name);

        if(!nearestNote.name.equals(prevNote.name)){

            allNotes.add(nearestNote);

            if(noteArray.size() == 14){
                noteArray.remove(0);
                noteArray.trimToSize();
            }

            noteArray.add(nearestNote);

           // if(slowDOWN%10==0)
            notesOnScreen(nearestNote);

            prevNote = nearestNote;
            //slowDOWN++;
        }



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


    public void notesOnScreen(Note note){

        int x = 700;
        int y = note.getyValue();
        int pos = 40;

        LinearInterpolator interpolator = new LinearInterpolator();

        RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.lowestLayer);

        ImageView image = new ImageView(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30,60);
        image.setLayoutParams(params);
        image.setX(x);
        image.setY(y);
        image.setMaxHeight(10);
        image.setMaxWidth(5);
        image.setBackgroundResource(R.drawable.singlenote);
        if(note.sharp){
            ImageView sharp = new ImageView(this);
            RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(20,40);
            sharp.setLayoutParams(para);
            sharp.setX(x - 15);
            sharp.setY(y + 32);
            sharp.setBackgroundResource(R.drawable.sharpnote);
            sharp.animate().x(pos - 15).setInterpolator(interpolator).setDuration(5500);
            theLayout.addView(sharp);
        }else if(note.flat){
            ImageView flat = new ImageView(this);
            RelativeLayout.LayoutParams paraFlat = new RelativeLayout.LayoutParams(10,30);
            flat.setLayoutParams(paraFlat);
            flat.setX(x - 10);
            flat.setY(y + 30);
            flat.setBackgroundResource(R.drawable.flatnote);
            flat.animate().x(pos - 10).setInterpolator(interpolator).setDuration(5500);
            theLayout.addView(flat);
        }

        image.animate().x(pos).setInterpolator(interpolator).setDuration(5500);

        theLayout.addView(image);
    }

}