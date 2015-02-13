package com.businesspanda.verynote;

import java.lang.Thread;
import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    Thread pitch_detector_thread_;
    public String written = " ";
    public ArrayList<String> noteArray = new ArrayList();

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
        final TextView setPrevNotes = (TextView) findViewById(R.id.prevNotesTextView);

        //String pitchString = Long.toString(Math.round(pitch));
        //System.out.println(pitchString);

        Integer pitchInt = (int) (pitch);
        //System.out.println(pitchInt + " PITCH INT");
        String nearestNote = NoteSearch.findNearestNote(pitchInt);

        System.out.println(nearestNote + "  nearest note :)");

        changeFreq.setText(nearestNote);


        String earlierNotes = (String) setPrevNotes.getText();

        if(!nearestNote.equals(written)){

            if(noteArray.size() == 14){
                noteArray.remove(0);
                noteArray.trimToSize();
            }

            noteArray.add(nearestNote);
            setPrevNotes.setText(noteArray.toString());

            /*
            noteArray.toString()

            setPrevNotes.setText(earlierNotes + " " + nearestNote);*/
            written = nearestNote;
        }



    }



}