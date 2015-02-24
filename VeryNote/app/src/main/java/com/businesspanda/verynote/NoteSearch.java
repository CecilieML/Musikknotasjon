package com.businesspanda.verynote;

import android.provider.ContactsContract;

import java.util.HashMap;

/**
 * Created by Helene on 10.02.2015.
 */
public class NoteSearch {

    public static HashMap<Integer, Note> notes = new HashMap<>();

    public static void createTable() {

        //HashMap to hold hertz values for notes, C1 to B6

        //C
        Note C3 = new Note(false, false, 131, 208, "C3");
        Note C4 = new Note(false, false, 262, 123, "C4");       //X
        Note C5 = new Note(false, false, 523, 79, "C5");        //X
        Note C6 = new Note(false, false, 1047, 35, "C6");       //

        // notes.put(33, "C1");
        // notes.put(65, "C2");
        notes.put(131, C3);
        notes.put(262, C4);
        notes.put(523, C5);
        notes.put(1047, C6);

        //C#
        Note C3s = new Note(true, false, 139, 208, "C3#");
        Note C4s = new Note(true, false, 277, 123, "C4#");      //X
        Note C5s = new Note(true, false, 554, 79, "C5#");       //X
        Note C6s = new Note(true, false, 1109, 35, "C6#");      //

        // notes.put(35, "C1#");
        // notes.put(69, "C2#");
        notes.put(139, C3s);
        notes.put(277, C4s);
        notes.put(554, C5s);
        notes.put(1109, C6s);

        //D
        Note D3 = new Note(false, false, 147, 202, "D3");
        Note D4 = new Note(false, false, 294, 116, "D4");       //X
        Note D5 = new Note(false, false, 587, 72, "D5");        //X
        Note D6 = new Note(false, false, 1175, 28, "D6");       //

        // notes.put(37, "D1");
        // notes.put(73, "D2");
        notes.put(147, D3);
        notes.put(294, D4);
        notes.put(587, D5);
        notes.put(1175, D6);

        //Eb
        Note E3b = new Note(false, true, 156, 196, "E3b");
        Note E4b = new Note(false, true, 311, 110, "E4b");      //X
        Note E5b = new Note(false, true, 622, 66, "E5b");       //X
        Note E6b = new Note(false, true, 1245, 22, "E6b");      //

        // notes.put(39, "E1b");
        // notes.put(78, "E2b");
        notes.put(156, E3b);
        notes.put(311, E4b);
        notes.put(622, E5b);
        notes.put(1245, E6b);

        //E
        Note E3 = new Note(false, false, 165, 196, "E3");
        Note E4 = new Note(false, false, 330, 110, "E4");       //X
        Note E5 = new Note(false, false, 659, 66, "E5");        //X
        Note E6 = new Note(false, false, 1319, 22, "E6");       //

        // notes.put(41, "E1");
        // notes.put(82, "E2");
        notes.put(165, E3);
        notes.put(330, E4);
        notes.put(659, E5);
        notes.put(1319, E6);

        //F
        Note F3 = new Note(false, false, 175, 190, "F3");
        Note F4 = new Note(false, false, 349, 104, "F4");       //X
        Note F5 = new Note(false, false, 698, 60, "F5");        //X
        Note F6 = new Note(false, false, 1397, 16, "F6");       //

        // notes.put(44, "F1");
        // notes.put(87, "F2");
        notes.put(175, F3);
        notes.put(349, F4);
        notes.put(698, F5);
        notes.put(1397, F6);

        //F#
        Note F3s = new Note(true, false, 185, 190, "F3#");
        Note F4s = new Note(true, false, 370, 104, "F4#");      //X
        Note F5s = new Note(true, false, 740, 60, "F5#");       //X
        Note F6s = new Note(true, false, 1480, 16, "F6#");      //

        // notes.put(46, "F1#");
        // notes.put(93, "F2#");
        notes.put(185, F3s);
        notes.put(370, F4s);
        notes.put(740, F5s);
        notes.put(1480, F6s);

        //G
        Note G3 = new Note(false, false, 196, 184, "G3");       //X
        Note G4 = new Note(false, false, 392, 98, "G4");        //X
        Note G5 = new Note(false, false, 784, 54, "G5");        //X
        Note G6 = new Note(false, false, 1568, 10, "G6");       //

        // notes.put(49, "G1");
        // notes.put(98, "G2");
        notes.put(196, G3);
        notes.put(392, G4);
        notes.put(784, G5);
        notes.put(1568, G6);

        //G#
        Note G3s = new Note(true, false, 208, 184, "G3#");      //X
        Note G4s = new Note(true, false, 415, 98, "G4#");       //X
        Note G5s = new Note(true, false, 831, 54, "G5#");       //X
        Note G6s = new Note(true, false, 1661, 10, "D6#");      //

        // notes.put(52, "G1#");
        // notes.put(104, "G2#");
        notes.put(208, G3s);
        notes.put(415, G4s);
        notes.put(831, G5s);
        notes.put(1661, G6s);

        //A
        Note A3 = new Note(false, false, 220, 178, "A3");       //X
        Note A4 = new Note(false, false, 440, 92, "A4");        //X
        Note A5 = new Note(false, false, 880, 48, "A5");        //
        Note A6 = new Note(false, false, 1760, 4, "A6");        //

        // notes.put(55, "A1");
        // notes.put(110, "A2");
        notes.put(220, A3);
        notes.put(440, A4);
        notes.put(880, A5);
        notes.put(1760, A6);

        //Bb
        Note B3b = new Note(false, true, 233, 172, "B3b");      //X
        Note B4b = new Note(false, true, 466, 86, "B4b");       //X
        Note B5b = new Note(false, true, 932, 42, "B5b");       //
        Note B6b = new Note(false, true, 1864, -2, "B6b");       //

        //  notes.put(58, "B1b");
        //  notes.put(117, "B2b");
        notes.put(233, B3b);
        notes.put(466, B4b);
        notes.put(932, B5b);
        notes.put(1864, B6b);

        //B
        Note B3 = new Note(false, false, 247, 172, "B3");       //X
        Note B4 = new Note(false, false, 493, 86, "B4");        //X
        Note B5 = new Note(false, false, 988, 42, "B5");        //
        Note B6 = new Note(false, false, 1976, -2, "B6");       //

      //  notes.put(62, "B1");
      //  notes.put(123, "B2");
        notes.put(247, B3);
        notes.put(493, B4);
        notes.put(988, B5);
        notes.put(1976, B6);

    }



    public static Note findNearestNote(Integer target) {
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
        //String nearestString = Long.toString(Math.round(nearest));
        //return nearestString;
    }

}
