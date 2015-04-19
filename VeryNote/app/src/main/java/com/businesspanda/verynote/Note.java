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
    String durationOfNote;

    public Note(boolean sharp, boolean flat, int freq, String name, int height, int nmbOfLinesTreble, int nmbOfLinesBass, String durationOfNote){
        this.sharp = sharp;
        this.flat = flat;
        this.freq = freq;
        this.name = name;
        this.height = height;
        this.nmbOfLinesTreble = nmbOfLinesTreble;
        this.nmbOfLinesBass = nmbOfLinesBass;
        this.durationOfNote = durationOfNote;
    }



    public boolean isSharp() {
        return sharp;
    }
    public void setSharp(boolean sharp) {
        this.sharp = sharp;
    }

    public boolean isFlat() {
        return flat;
    }

    public int getFreq() {
        return freq;
    }
    public void setFlat(boolean flat) {
        this.flat = flat;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getNoteHeight(){
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getNmbOfLinesTreble() {
        return nmbOfLinesTreble;
    }
    public void setNmbOfLinesTreble(int nmbOfLinesTreble) {
        this.nmbOfLinesTreble = nmbOfLinesTreble;
    }

    public int getNmbOfLinesBass() {
        return nmbOfLinesBass;
    }
    public void setNmbOfLinesBass(int nmbOfLinesBass) {
        this.nmbOfLinesBass = nmbOfLinesBass;
    }

    public String getDurationOfNote() {
        return durationOfNote;
    }
    public void setDurationOfNote(String durationOfNote) {
        this.durationOfNote = durationOfNote;
    }







}
