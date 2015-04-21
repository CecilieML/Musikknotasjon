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

import android.util.TypedValue;

// Places all height values in an array in ascending order
public class yValueSearch {

    public static float[] yValues = new float[28];

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

        yValues[27] = c3;
        yValues[20] = c4;
        yValues[13] = c5;
        yValues[6] = c6;

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

        yValues[26] = d3;
        yValues[19] = d4;
        yValues[12] = d5;
        yValues[5]  = d6;

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

        yValues[25] = e3;
        yValues[18] = e4;
        yValues[11] = e5;
        yValues[4]  = e6;

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

        yValues[24] = f3;
        yValues[17] = f4;
        yValues[10] = f5;
        yValues[3]  = f6;

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

        yValues[23] = g3;
        yValues[16] = g4;
        yValues[9]  = g5;
        yValues[2]  = g6;

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

        yValues[22] = a3;
        yValues[15] = a4;
        yValues[8]  = a5;
        yValues[1]  = a6;

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

        yValues[21] = b3;
        yValues[14] = b4;
        yValues[7]  = b5;
        yValues[0]  = b6;

    }

    // Returns the notes index in this array based on a height value
    public static int returnIdx(float valueToFind){
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
        return (indexOfValueToFind);
    }



}
