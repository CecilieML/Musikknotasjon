package com.businesspanda.verynote;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Helene on 16.02.2015.
 */

public class Metronome implements Runnable {

    Switch metronomeswitch;

    public Metronome(View view){
        metronomeswitch = (Switch) view;
    }

    private Handler mHandler = new Handler();

    public void run() {
           // RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
           // Switch metronomeswitch = (Switch) layout.findViewById(R.id.metronomeswitch);
            boolean on = metronomeswitch.isChecked();
            System.out.println("is it on?    " + on);
            if (on) {
                //mHandler.removeCallbacks();
                //mHandler.postDelayed(metronomeClock, 1000);
                // do things
                //Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                //v.vibrate(500);
                System.out.println("ON! " + on);

            } else {
                //mHandler.removeCallbacks(metronomeClock);
                // stop doing things
                System.out.println("OFF!" + on);
            }
    }

}