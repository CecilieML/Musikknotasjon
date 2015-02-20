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

   /* public boolean sharp(String sharpFlat) {
        String noteName = note.getName;
        sharpFlat = noteName.substring(noteName.length() - 1);

        if (sharpFlat.equals("#")) {
            sharp = true;
        } else {
            sharp = false;
        }
    }

    public boolean flat(String sharpFlat) {
        String noteName = note.getName;
        sharpFlat = noteName.substring(noteName.length() - 1);

        if (sharpFlat.equals("b")) {
            flat = true;
        } else {
            flat = false;
        }
    }*/

}
