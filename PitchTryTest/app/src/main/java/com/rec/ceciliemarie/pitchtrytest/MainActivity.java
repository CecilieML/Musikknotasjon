package com.rec.ceciliemarie.pitchtrytest;

import java.lang.Thread;
import java.util.HashMap;

import com.rec.ceciliemarie.pitchtrytest.PitchDec;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    Thread pitch_detector_thread_;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    public void ShowPitchDetectionResult( final double pitch) {

        final TextView changeFreq = (TextView) findViewById(R.id.freqTextview);
        String pitchString = Long.toString(Math.round(pitch));
        System.out.println(pitchString);
        changeFreq.setText(pitchString);

    }

    public void prevNotes(View view){
        final TextView setPrevNotes = (TextView) findViewById(R.id.prevNotesTextView);
        String earlierNotes = (String) setPrevNotes.getText();
        setPrevNotes.setText(earlierNotes + " " + "CURRENT NOTE"); /** CHANGE THIS WHEN ABLE TO GET CURRENT NOTE*/
        //SHOULD ONLY BE CALLED WHEN NOTE CHANGES??
    }

}
