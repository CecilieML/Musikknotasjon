package com.businesspanda.verynote;

/** Copyright (C) 2015 by BusinessPanda - Cecilie M. Langfeldt, Helene H. Larsen.
 **
 ** Permission to use, copy, modify, and distribute this software and its
 ** documentation for any purpose and without fee is hereby granted, provided
 ** that the above copyright notice appear in all copies and that both that
 ** copyright notice and this permission notice appear in supporting
 ** documentation.  This software is provided "as is" without express or
 ** implied warranty.
 */

public class Note {

    boolean sharp;
    boolean flat;
    int freq;
    String name;
    int height;
    int nmbOfLinesTreble;
    int nmbOfLinesBass;


    public Note(boolean sharp, boolean flat, int freq, String name, int height, int nmbOfLinesTreble, int nmbOfLinesBass){
        this.sharp = sharp;
        this.flat = flat;
        this.freq = freq;
        this.name = name;
        this.height = height;
        this.nmbOfLinesTreble = nmbOfLinesTreble;
        this.nmbOfLinesBass = nmbOfLinesBass;

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

    public int getNmbOfLinesBass() {
        return nmbOfLinesBass;
    }
}
