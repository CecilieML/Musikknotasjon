package com.rec.ceciliemarie.pitchtrytest;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by CecilieMarie on 09.02.2015.
 */
public class DrawView extends View {

    private HashMap<Double, Double> frequencies_;
    private double pitch_;
    private PitchDecRep representation_;

    private Handler handler_;
    private Timer timer_;

    public DrawView(Context context) {
        super(context);

        // UI update cycle.
        handler_ = new Handler();
        timer_ = new Timer();
        timer_.schedule(new TimerTask() {
                            public void run() {
                                handler_.post(new Runnable() {
                                    public void run() {
                                        invalidate();
                                    }
                                });
                            }
                        },
                UI_UPDATE_MS ,
                UI_UPDATE_MS );
    }

    /*****************************************************************************************/

    public void setDetectionResults(final HashMap<Double, Double> frequencies, double pitch) {
        frequencies_ = frequencies;
        pitch_ = pitch;
        System.out.println("pitch" + pitch_);
        System.out.println("freq" + frequencies_);
    }

    /*****************************************************************************************/


    /* *************************** */
    private final static int MIN_AMPLITUDE = 40000;
    private final static int MAX_AMPLITUDE = 3200000;
    private final static double MAX_PITCH_DIFF = 20;  // in Hz
    private final static int UI_UPDATE_MS = 100;
    /* ************************** */



    public void setFreq(HashMap freq, double pit){
        final TextView changeFreq = (TextView) findViewById(R.id.freqTextview);
        //changeFreq.setText(Math.round(representation_.pitch * 10) / 10.0 + " Hz");
    }

}
