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
    int height;
    int nmbOfLinesTreble;
    int nmbOfLinesBass;


    public Note(boolean sharp, boolean flat, int freq, int yValue, String name, int height, int nmbOfLinesTreble, int nmbOfLinesBass){
        this.sharp = sharp;
        this.flat = flat;
        this.freq = freq;
        this.yValue = yValue;
        this.name = name;
        this.height = height;
        this.nmbOfLinesTreble = nmbOfLinesTreble;
        this.nmbOfLinesBass = nmbOfLinesBass;

    }

    public boolean sharp(String sharpFlat) {
        return sharp;
    }

    public boolean flat(String sharpFlat) {
        return flat;
    }

    public int getyValue() {
        return yValue;
    }

    public int getFreq() {
        return freq;
    }

    public String getName() {
        return name;
    }

    public int getNoteHeight(){
        return height;
    }

    public int getNmbOfLinesTreble() {
        return nmbOfLinesTreble;
    }
}
