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

    //HashMap<Integer, String> notes = new HashMap<>();

        notes.put(16, "C0");
        notes.put(33, "C1");
        notes.put(65, "C2");
        notes.put(131, "C3");
        notes.put(262, "C4");
        notes.put(523, "C5");
        notes.put(1047, "C6");
        notes.put(2093, "C7");
        notes.put(4186, "C8");

        notes.put(17, "C0#");
        notes.put(35, "C1#");
        notes.put(69, "C2#");
        notes.put(139, "C3#");
        notes.put(277, "C4#");
        notes.put(554, "C5#");
        notes.put(1109, "C6#");
        notes.put(2217, "C7#");
        notes.put(4435, "C8#");

        notes.put(16, "D0");
        notes.put(33, "D1");
        notes.put(65, "D2");
        notes.put(131, "D3");
        notes.put(262, "D4");
        notes.put(523, "D5");
        notes.put(1047, "D6");
        notes.put(2093, "D7");
        notes.put(4186, "D8");

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
