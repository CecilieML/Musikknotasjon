package com.businesspanda.verynote;

/** Copyright (C) 2015 by BusinessPanda.
 **
 ** Permission to use, copy, modify, and distribute this software and its
 ** documentation for any purpose and without fee is hereby granted, provided
 ** that the above copyright notice appear in all copies and that both that
 ** copyright notice and this permission notice appear in supporting
 ** documentation.  This software is provided "as is" without express or
 ** implied warranty.
 */

import android.util.TypedValue;


public class yValueSearch {

    public static float[] yValues = new float[48];

    public static void createYValues() {

        //C
        TypedValue c3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.C3, c3outValue, true);
        float c3 = c3outValue.getFloat();

        TypedValue c4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.C4, c4outValue, true);
        float c4 = c4outValue.getFloat();

        TypedValue c5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.C5, c5outValue, true);
        float c5 = c5outValue.getFloat();

        TypedValue c6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.C6, c6outValue, true);
        float c6 = c6outValue.getFloat();

        yValues[47] = c3;
        yValues[35] = c4;
        yValues[23] = c5;
        yValues[11] = c6;

        //C#
        TypedValue cs3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Cs3, cs3outValue, true);
        float cs3 = cs3outValue.getFloat();

        TypedValue cs4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Cs4, cs4outValue, true);
        float cs4 = cs4outValue.getFloat();

        TypedValue cs5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Cs5, cs5outValue, true);
        float cs5 = cs5outValue.getFloat();

        TypedValue cs6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Cs6, cs6outValue, true);
        float cs6 = cs6outValue.getFloat();

        yValues[46] = cs3;
        yValues[34] = cs4;
        yValues[22] = cs5;
        yValues[10] = cs6;

        //D
        TypedValue d3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.D3, d3outValue, true);
        float d3 = d3outValue.getFloat();

        TypedValue d4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.D4, d4outValue, true);
        float d4 = d4outValue.getFloat();

        TypedValue d5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.D5, d5outValue, true);
        float d5 = d5outValue.getFloat();

        TypedValue d6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.D6, d6outValue, true);
        float d6 = d6outValue.getFloat();

        yValues[45] = d3;
        yValues[33] = d4;
        yValues[21] = d5;
        yValues[9]  = d6;

       //Eb
        TypedValue eb3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Eb3, eb3outValue, true);
        float eb3 = eb3outValue.getFloat();

        TypedValue eb4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Eb4, eb4outValue, true);
        float eb4 = eb4outValue.getFloat();

        TypedValue eb5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Eb5, eb5outValue, true);
        float eb5 = eb5outValue.getFloat();

        TypedValue eb6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Eb6, eb6outValue, true);
        float eb6 = eb6outValue.getFloat();

        yValues[44] = eb3;
        yValues[32] = eb4;
        yValues[20] = eb5;
        yValues[8]  = eb6;

        //E
        TypedValue e3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.E3, e3outValue, true);
        float e3 = e3outValue.getFloat();

        TypedValue e4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.E4, e4outValue, true);
        float e4 = e4outValue.getFloat();

        TypedValue e5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.E5, e5outValue, true);
        float e5 = e5outValue.getFloat();

        TypedValue e6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.E6, e6outValue, true);
        float e6 = e6outValue.getFloat();

        yValues[43] = e3;
        yValues[31] = e4;
        yValues[19] = e5;
        yValues[7]  = e6;

        //F
        TypedValue f3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.F3, f3outValue, true);
        float f3 = f3outValue.getFloat();

        TypedValue f4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.F4, f4outValue, true);
        float f4 = f4outValue.getFloat();

        TypedValue f5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.F5, f5outValue, true);
        float f5 = f5outValue.getFloat();

        TypedValue f6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.F6, f6outValue, true);
        float f6 = f6outValue.getFloat();

        yValues[42] = f3;
        yValues[30] = f4;
        yValues[18] = f5;
        yValues[6]  = f6;

        //F#
        TypedValue fs3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Fs3, fs3outValue, true);
        float fs3 = fs3outValue.getFloat();

        TypedValue fs4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Fs4, fs4outValue, true);
        float fs4 = fs4outValue.getFloat();

        TypedValue fs5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Fs5, fs5outValue, true);
        float fs5 = fs5outValue.getFloat();

        TypedValue fs6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Fs6, fs6outValue, true);
        float fs6 = fs6outValue.getFloat();

        yValues[41] = fs3;
        yValues[29] = fs4;
        yValues[17] = fs5;
        yValues[5]  = fs6;

        //G
        TypedValue g3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.G3, g3outValue, true);
        float g3 = g3outValue.getFloat();

        TypedValue g4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.G4, g4outValue, true);
        float g4 = g4outValue.getFloat();

        TypedValue g5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.G5, g5outValue, true);
        float g5 = g5outValue.getFloat();

        TypedValue g6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.G6, g6outValue, true);
        float g6 = g6outValue.getFloat();

        yValues[40] = g3;
        yValues[28] = g4;
        yValues[16] = g5;
        yValues[4]  = g6;

        //G#
        TypedValue gs3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Gs3, gs3outValue, true);
        float gs3 = gs3outValue.getFloat();

        TypedValue gs4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Gs4, gs4outValue, true);
        float gs4 = gs4outValue.getFloat();

        TypedValue gs5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Gs5, gs5outValue, true);
        float gs5 = gs5outValue.getFloat();

        TypedValue gs6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Gs6, gs6outValue, true);
        float gs6 = gs6outValue.getFloat();

        yValues[39] = gs3;
        yValues[27] = gs4;
        yValues[15] = gs5;
        yValues[3]  = gs6;

        //A
        TypedValue a3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.A3, a3outValue, true);
        float a3 = a3outValue.getFloat();

        TypedValue a4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.A4, a4outValue, true);
        float a4 = a4outValue.getFloat();

        TypedValue a5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.A5, a5outValue, true);
        float a5 = a5outValue.getFloat();

        TypedValue a6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.A6, a6outValue, true);
        float a6 = a6outValue.getFloat();

        yValues[38] = a3;
        yValues[26] = a4;
        yValues[14] = a5;
        yValues[2]  = a6;

        //Bb
        TypedValue bb3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Bb3, bb3outValue, true);
        float bb3 = bb3outValue.getFloat();

        TypedValue bb4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Bb4, bb4outValue, true);
        float bb4 = bb4outValue.getFloat();

        TypedValue bb5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Bb5, bb5outValue, true);
        float bb5 = bb5outValue.getFloat();

        TypedValue bb6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Bb6, bb6outValue, true);
        float bb6 = bb6outValue.getFloat();

        yValues[37] = bb3;
        yValues[25] = bb4;
        yValues[13] = bb5;
        yValues[1]  = bb6;

        //B
        TypedValue b3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.B3, b3outValue, true);
        float b3 = b3outValue.getFloat();

        TypedValue b4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.B4, b4outValue, true);
        float b4 = b4outValue.getFloat();

        TypedValue b5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.B5, b5outValue, true);
        float b5 = b5outValue.getFloat();

        TypedValue b6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.B6, b6outValue, true);
        float b6 = b6outValue.getFloat();

        yValues[36] = b3;
        yValues[24] = b4;
        yValues[12] = b5;
        yValues[0]  = b6;

    }


    public static int returnNext(float valueToFind){
        float percentValue = FitToScreen.returnPercent(valueToFind);

        double minDiff = Double.MAX_VALUE;

        int indexOfValueToFind = 0;

        for (int i = 0; i < yValues.length; i++) {
            double diff = Math.abs(percentValue - yValues[i]);
            if (diff < minDiff) {
                indexOfValueToFind = i;
                minDiff = diff;
            }
        }
        return indexOfValueToFind - 1;

    }

    public static int returnPrev(float valueToFind){
        float percentValue = FitToScreen.returnPercent(valueToFind);

        double minDiff = Double.MAX_VALUE;

        int indexOfValueToFind = 0;

        for (int i = 0; i < yValues.length; i++) {
            double diff = Math.abs(percentValue - yValues[i]);
            if (diff < minDiff) {
                indexOfValueToFind = i;
                minDiff = diff;
            }
        }
        return indexOfValueToFind - 1;
    }


    public static int findYIndex(float valueToFind) {

        float percentValue = FitToScreen.returnPercent(valueToFind);

        System.out.println(valueToFind + "   valuetofind");

        System.out.println(percentValue + "   percentvalue");

        double minDiff = Double.MAX_VALUE;

        int indexOfValueToFind = -1; //never return -1 I don't know how to fix this ...

        //also this should be slightly different for notes that are up side down :(

        for (int i = 0; i < yValues.length; i++) {
            double diff = Math.abs(percentValue - yValues[i]);
            if (diff < minDiff) {
                indexOfValueToFind = i;
                minDiff = diff;
            }
        }
        System.out.println(yValues[22]  + "   is this 0?");
        System.out.println(yValues[indexOfValueToFind] + " yvalues at index");
        System.out.println(indexOfValueToFind + "  <--- index  " + minDiff);
        return indexOfValueToFind;
    }
}
