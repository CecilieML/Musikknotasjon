package com.businesspanda.verynote;

/**
 * Created by Helene on 20.02.2015.
 */
public class Note {

    boolean sharp;
    boolean flat;
    int freq;
    int yValue;
    String name;

    public Note(boolean sharp, boolean flat, int freq, int yValue, String name){
        this.sharp = sharp;
        this.flat = flat;
        this.freq = freq;
        this.yValue = yValue;
        this.name = name;

    }

}
