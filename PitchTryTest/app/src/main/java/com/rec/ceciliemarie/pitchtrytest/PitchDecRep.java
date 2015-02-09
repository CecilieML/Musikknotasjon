package com.rec.ceciliemarie.pitchtrytest;


import android.util.Log;

/**
 * Created by CecilieMarie on 09.02.2015.
 */


public class PitchDecRep {
    PitchDecRep(double pitch_, int string_no_, int fret_) {
        pitch = pitch_; string_no = string_no_; fret = fret_;
        creation_date_ = System.currentTimeMillis();
        string_detected = true;
    }

    PitchDecRep(double pitch_) {
        pitch = pitch_;
        creation_date_ = System.currentTimeMillis();
        string_detected = false;
    }

    public int GetAlpha() {
        final long age = System.currentTimeMillis() - creation_date_;
        if (age > LIFE_TIME_MS) return 0;
        if (age < BRIGHT_TIME_MS) return 255;
        return (int) Math.floor(255 - (age - BRIGHT_TIME_MS) * 1.0 /
                (LIFE_TIME_MS - BRIGHT_TIME_MS) * 255);
    }


    public double pitch;
    public int string_no;
    public int fret;
    public boolean string_detected;
    private long creation_date_;

    private final static int LIFE_TIME_MS = 4000;
    private final static int BRIGHT_TIME_MS = 2000;

}


