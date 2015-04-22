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

import java.util.HashMap;


public class NoteSearch {

    public static HashMap<Integer, Note> notes = new HashMap<>();

    public static void createTable() {

        //Creates note objects to hold sharp/flat value, frequency in hertz, name, height on screen and values for lines.
        //Creates HashMap to hold note names and hertz values for searching.

        //C
        Note C3 = new Note(false, false, false, 131, "C3", 47, 4, 0, "");
        Note C4 = new Note(false, false, false, 262, "C4", 35, 1, 1, "");
        Note C5 = new Note(false, false, false, 523, "C5", 23, 0, 4, "");
        Note C6 = new Note(false, false, false, 1047, "C6", 11, 2, 8, "");

        notes.put(131, C3);
        notes.put(262, C4);
        notes.put(523, C5);
        notes.put(1047, C6);

        //C#
        Note C3s = new Note(true, false, false, 139, "Cs3", 46, 4, 0, "");
        Note C4s = new Note(true, false, false, 277, "Cs4", 34, 1, 1, "");
        Note C5s = new Note(true, false, false, 554, "Cs5", 22, 0, 4, "");
        Note C6s = new Note(true, false, false, 1109, "Cs6", 10, 2, 8, "");

        notes.put(139, C3s);
        notes.put(277, C4s);
        notes.put(554, C5s);
        notes.put(1109, C6s);

        //D
        Note D3 = new Note(false, false, false, 147, "D3", 45, 4, 0, "");
        Note D4 = new Note(false, false, false, 294, "D4", 33, 0, 1, "");
        Note D5 = new Note(false, false, false, 587, "D5", 21, 0, 5, "");
        Note D6 = new Note(false, false, false, 1175, "D6", 9, 2, 8, "");

        notes.put(147, D3);
        notes.put(294, D4);
        notes.put(587, D5);
        notes.put(1175, D6);

        //Eb
        Note E3b = new Note(false, true, false, 156, "Eb3", 44, 3, 0, "");
        Note E4b = new Note(false, true, false, 311, "Eb4", 32, 0, 2, "");
        Note E5b = new Note(false, true, false, 622, "Eb5", 20, 0, 5, "");
        Note E6b = new Note(false, true, false, 1245, "Eb6", 8, 3, 9, "");

        notes.put(156, E3b);
        notes.put(311, E4b);
        notes.put(622, E5b);
        notes.put(1245, E6b);

        //E
        Note E3 = new Note(false, false, false, 165, "E3", 43, 3, 0, "");
        Note E4 = new Note(false, false, false, 330, "E4", 31, 0, 2, "");
        Note E5 = new Note(false, false, false, 659, "E5", 19, 0, 5, "");
        Note E6 = new Note(false, false, false, 1319, "E6", 7, 3, 9, "");

        notes.put(165, E3);
        notes.put(330, E4);
        notes.put(659, E5);
        notes.put(1319, E6);

        //F
        Note F3 = new Note(false, false, false, 175, "F3", 42, 3, 0, "");
        Note F4 = new Note(false, false, false, 349, "F4", 30, 0, 2, "");
        Note F5 = new Note(false, false, false, 698, "F5", 18, 0, 6, "");
        Note F6 = new Note(false, false, false, 1397, "F6", 6, 3, 9, "");

        notes.put(175, F3);
        notes.put(349, F4);
        notes.put(698, F5);
        notes.put(1397, F6);

        //F#
        Note F3s = new Note(true, false, false, 185, "Fs3", 41, 3, 0, "");
        Note F4s = new Note(true, false, false, 370, "Fs4", 29, 0, 2, "");
        Note F5s = new Note(true, false, false, 740, "Fs5", 17, 0, 6, "");
        Note F6s = new Note(true, false, false, 1480, "Fs6", 5, 3, 9, "");

        notes.put(185, F3s);
        notes.put(370, F4s);
        notes.put(740, F5s);
        notes.put(1480, F6s);

        //G
        Note G3 = new Note(false, false, false, 196, "G3", 40, 2, 0, "");
        Note G4 = new Note(false, false, false, 392, "G4", 28, 0, 3, "");
        Note G5 = new Note(false, false, false, 784, "G5", 16, 0, 6, "");
        Note G6 = new Note(false, false, false, 1568, "G6", 4, 4, 10, "");

        notes.put(196, G3);
        notes.put(392, G4);
        notes.put(784, G5);
        notes.put(1568, G6);

        //G#
        Note G3s = new Note(true, false, false, 208, "Gs3", 39, 2, 0, "");
        Note G4s = new Note(true, false, false, 415, "Gs4", 27, 0, 3, "");
        Note G5s = new Note(true, false, false, 831, "Gs5", 15, 0, 6, "");
        Note G6s = new Note(true, false, false, 1661, "Ds6", 3, 4, 10, "");

        notes.put(208, G3s);
        notes.put(415, G4s);
        notes.put(831, G5s);
        notes.put(1661, G6s);

        //A
        Note A3 = new Note(false, false, false, 220, "A3", 38, 2, 0, "");
        Note A4 = new Note(false, false, false, 440, "A4", 26, 0, 3, "");
        Note A5 = new Note(false, false, false, 880, "A5", 14, 1, 7, "");
        Note A6 = new Note(false, false, false, 1760, "A6", 2, 4, 10, "");

        notes.put(220, A3);
        notes.put(440, A4);
        notes.put(880, A5);
        notes.put(1760, A6);

        //Bb
        Note B3b = new Note(false, true, false, 233, "Bb3", 37, 1, 0, "");
        Note B4b = new Note(false, true, false, 466, "Bb4", 25, 0, 4, "");
        Note B5b = new Note(false, true, false, 932, "Bb5", 13, 1, 7, "");
        Note B6b = new Note(false, true, false, 1864, "Bb6", 1, 5, 11, "");

        notes.put(233, B3b);
        notes.put(466, B4b);
        notes.put(932, B5b);
        notes.put(1864, B6b);

        //B
        Note B3 = new Note(false, false, false, 247, "B3", 36, 1, 0, "");
        Note B4 = new Note(false, false, false, 493, "B4", 24, 0, 4, "");
        Note B5 = new Note(false, false, false, 988, "B5", 12, 1, 7, "");
        Note B6 = new Note(false, false, false, 1976, "B6", 0, 5, 11, "");

        notes.put(247, B3);
        notes.put(493, B4);
        notes.put(988, B5);
        notes.put(1976, B6);


    }

    public static Note findNearestNote(Integer target) {
        //Searches through HashMap to find the note with the least difference between input hertz value and note hertz value.
        Note closestNote;
        double minDiff = Double.MAX_VALUE;
        int nearest = 0;
        for (int key : notes.keySet()) {
            double diff = Math.abs((double) target - (double) key);
            if (diff < minDiff) {
                nearest = key;
                minDiff = diff;
            }
        }
        closestNote = notes.get(nearest);
        return closestNote;
    }

    public static Note findNoteByName(String noteName){
        //Searches through HashMap to find the note with the same name as the input and returns it.
        Note noteToReturn = null;
        for(Note note : notes.values()){
            if(note.getName().equals(noteName)){
                noteToReturn = note;
            }
        }
        return noteToReturn;
    }

}
