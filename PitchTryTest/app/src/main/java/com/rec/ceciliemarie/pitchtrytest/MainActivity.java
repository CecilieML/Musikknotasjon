package com.rec.ceciliemarie.pitchtrytest;

import java.lang.Thread;
import java.util.HashMap;

import com.rec.ceciliemarie.pitchtrytest.DrawView;
import com.rec.ceciliemarie.pitchtrytest.PitchDec;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;


import java.util.HashMap;


public class MainActivity extends ActionBarActivity {



    public DrawView tv_;
    Thread pitch_detector_thread_;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_ = new DrawView(this);
        //setContentView(R.layout.main);
        setContentView(tv_);
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

    public void ShowPitchDetectionResult(
            final HashMap<Double, Double> frequencies,
            final double pitch) {
        tv_.setDetectionResults(frequencies, pitch);
    }
}
