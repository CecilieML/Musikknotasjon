package com.businesspanda.verynote;

import java.lang.Thread;
import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    Thread pitch_detector_thread_;
    public String written = " ";
    public ArrayList<String> noteArray = new ArrayList();
    public ArrayList<String> allNotes = new ArrayList();
    public boolean sharp = false;
    public boolean flat = false;
    public int y;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoteSearch.createTable();
    }

    @Override
    public void onStart() {
        super.onStart();
        pitch_detector_thread_ = new Thread(new PitchDec(this, new Handler()));
        pitch_detector_thread_.start();
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
      //  final TextView setPrevNotes = (TextView) findViewById(R.id.prevNotesTextView);

        //String pitchString = Long.toString(Math.round(pitch));
        //System.out.println(pitchString);

        Integer pitchInt = (int) (pitch);
        //System.out.println(pitchInt + " PITCH INT");
        String nearestNote = NoteSearch.findNearestNote(pitchInt);

        System.out.println(nearestNote + "  nearest note :)");

        changeFreq.setText(nearestNote);


       // String earlierNotes = (String) setPrevNotes.getText();

        if(!nearestNote.equals(written)){

            /***/
            allNotes.add(nearestNote);
            /***/

            if(noteArray.size() == 14){
                noteArray.remove(0);
                noteArray.trimToSize();
            }

            noteArray.add(nearestNote);
          //  setPrevNotes.setText(noteArray.toString());

            /*
            noteArray.toString()

            setPrevNotes.setText(earlierNotes + " " + nearestNote);*/

           // ImageView treble = (ImageView) findViewById(R.id.treble);

            String sharpFlat = nearestNote.substring(nearestNote.length()-1);

            if(sharpFlat.equals("#")){
              //  sharp = true;
            }else {
                sharp = false;
            }

            if(sharpFlat.equals("b")){
                flat = true;
            }else {
                flat = false;
            }

            notesOnScreen();

            written = nearestNote;
        }



    }

    public void notesOnScreen(){

        int x = 700;
        int y = 110;

        RelativeLayout theLayout = (RelativeLayout) findViewById(R.id.layout);

        ImageView image = new ImageView(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(30,60);
        image.setLayoutParams(params);
        image.setX(x);
        image.setY(y);
        image.setMaxHeight(10);
        image.setMaxWidth(5);
        image.setBackgroundResource(R.drawable.note);
        if(sharp){
            ImageView sharp = new ImageView(this);
            RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(30,30);
            sharp.setLayoutParams(para);
            sharp.setX(x);
            sharp.setY(y - 10);
            sharp.setBackgroundResource(R.drawable.ic_launcher);
            sharp.animate().x(-400).setDuration(10000);
            theLayout.addView(sharp);
        }else if(flat){
            ImageView flat = new ImageView(this);
            RelativeLayout.LayoutParams paraFlat = new RelativeLayout.LayoutParams(20,20);
            flat.setLayoutParams(paraFlat);
            flat.setX(x);
            flat.setY(y - 10);
            flat.setBackgroundResource(R.drawable.ic_launcher);
            flat.animate().x(-400).setDuration(10000);
            theLayout.addView(flat);
        }
        image.animate().x(-400).setDuration(10000);

       // image.setImageDrawable(draw);
        theLayout.addView(image);
    }

}