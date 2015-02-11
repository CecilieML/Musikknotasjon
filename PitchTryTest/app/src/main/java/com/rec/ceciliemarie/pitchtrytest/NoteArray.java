package com.rec.ceciliemarie.pitchtrytest;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.TreeMap;

/**
 * Created by Helene on 10.02.2015.
 */
public class NoteArray {

    public static HashMap<Integer, String> notes = new HashMap<>();

    public static void createTable() {

    //HashMap to hold hertz values for notes, C1 to B6

        notes.put(33, "C1");
        notes.put(65, "C2");
        notes.put(131, "C3");
        notes.put(262, "C4");
        notes.put(523, "C5");
        notes.put(1047, "C6");

        notes.put(35, "C1#");
        notes.put(69, "C2#");
        notes.put(139, "C3#");
        notes.put(277, "C4#");
        notes.put(554, "C5#");
        notes.put(1109, "C6#");

        notes.put(37, "D1");
        notes.put(73, "D2");
        notes.put(147, "D3");
        notes.put(294, "D4");
        notes.put(587, "D5");
        notes.put(1175, "D6");

        notes.put(39, "D1#");
        notes.put(78, "D2#");
        notes.put(156, "D3#");
        notes.put(311, "D4#");
        notes.put(622, "D5#");
        notes.put(1245, "D6#");

        notes.put(41, "E1");
        notes.put(82, "E2");
        notes.put(165, "E3");
        notes.put(330, "E4");
        notes.put(659, "E5");
        notes.put(1319, "E6");

        notes.put(44, "F1");
        notes.put(87, "F2");
        notes.put(175, "F3");
        notes.put(349, "F4");
        notes.put(698, "F5");
        notes.put(1397, "F6");

        notes.put(46, "F1#");
        notes.put(93, "F2#");
        notes.put(185, "F3#");
        notes.put(370, "F4#");
        notes.put(740, "F5#");
        notes.put(1480, "F6#");

        notes.put(49, "G1");
        notes.put(98, "G2");
        notes.put(196, "G3");
        notes.put(392, "G4");
        notes.put(784, "G5");
        notes.put(1568, "G6");

        notes.put(52, "G1#");
        notes.put(104, "G2#");
        notes.put(208, "G3#");
        notes.put(415, "G4#");
        notes.put(831, "G5#");
        notes.put(1661, "G6#");

        notes.put(55, "A1");
        notes.put(110, "A2");
        notes.put(220, "A3");
        notes.put(440, "A4");
        notes.put(880, "A5");
        notes.put(1760, "A6");

        notes.put(58, "A1#");
        notes.put(117, "A2#");
        notes.put(233, "A3#");
        notes.put(466, "A4#");
        notes.put(932, "A5#");
        notes.put(1864, "A6#");

        notes.put(62, "B1");
        notes.put(123, "B2");
        notes.put(247, "B3");
        notes.put(493, "B4");
        notes.put(988, "B5");
        notes.put(1976, "B6");

    }

    public static String findNearestNote(Integer target) {
        String closestNote;
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
