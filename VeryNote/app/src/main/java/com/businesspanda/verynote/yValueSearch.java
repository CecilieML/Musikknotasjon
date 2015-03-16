package com.businesspanda.verynote;

import android.util.TypedValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CecilieMarie on 11.03.2015.
 */
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
        Config.context.getResources().getValue(R.dimen.Cs3, c3outValue, true);
        float cs3 = cs3outValue.getFloat();

        TypedValue cs4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Cs4, c4outValue, true);
        float cs4 = cs4outValue.getFloat();

        TypedValue cs5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Cs5, c5outValue, true);
        float cs5 = cs5outValue.getFloat();

        TypedValue cs6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.Cs6, c6outValue, true);
        float cs6 = cs6outValue.getFloat();

        yValues[46] = cs3;
        yValues[34] = cs4;
        yValues[22] = cs5;
        yValues[10] = cs6;

        //D
        TypedValue d3outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.D3, c3outValue, true);
        float d3 = d3outValue.getFloat();

        TypedValue d4outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.D4, c4outValue, true);
        float d4 = d4outValue.getFloat();

        TypedValue d5outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.D5, c5outValue, true);
        float d5 = d5outValue.getFloat();

        TypedValue d6outValue = new TypedValue();
        Config.context.getResources().getValue(R.dimen.D6, c6outValue, true);
        float d6 = d6outValue.getFloat();

        yValues[45] = d3;
        yValues[33] = d4;
        yValues[21] = d5;
        yValues[9]  = d6;

     /*   //Eb
        yValues[44] = (int) Config.context.getResources().getDimension(R.dimen.Eb3);
        yValues[32] = (int) Config.context.getResources().getDimension(R.dimen.Eb4);
        yValues[20] = (int) Config.context.getResources().getDimension(R.dimen.Eb5);
        yValues[8]  = (int) Config.context.getResources().getDimension(R.dimen.Eb6);

        //E
        yValues[43] = (int) Config.context.getResources().getDimension(R.dimen.E3);
        yValues[31] = (int) Config.context.getResources().getDimension(R.dimen.E4);
        yValues[19] = (int) Config.context.getResources().getDimension(R.dimen.E5);
        yValues[7]  = (int) Config.context.getResources().getDimension(R.dimen.E6);

        //F
        yValues[42] = (int) Config.context.getResources().getDimension(R.dimen.F3);
        yValues[30] = (int) Config.context.getResources().getDimension(R.dimen.F4);
        yValues[18] = (int) Config.context.getResources().getDimension(R.dimen.F5);
        yValues[6]  = (int) Config.context.getResources().getDimension(R.dimen.F6);

        //F#
        yValues[41] = (int) Config.context.getResources().getDimension(R.dimen.Fs3);
        yValues[29] = (int) Config.context.getResources().getDimension(R.dimen.Fs4);
        yValues[17] = (int) Config.context.getResources().getDimension(R.dimen.Fs5);
        yValues[5]  = (int) Config.context.getResources().getDimension(R.dimen.Fs6);

        //G
        yValues[40] = (int) Config.context.getResources().getDimension(R.dimen.G3);
        yValues[28] = (int) Config.context.getResources().getDimension(R.dimen.G4);
        yValues[16] = (int) Config.context.getResources().getDimension(R.dimen.G5);
        yValues[4]  = (int) Config.context.getResources().getDimension(R.dimen.G6);

        //G#
        yValues[39] = (int) Config.context.getResources().getDimension(R.dimen.Gs3);
        yValues[27] = (int) Config.context.getResources().getDimension(R.dimen.Gs4);
        yValues[15] = (int) Config.context.getResources().getDimension(R.dimen.Gs5);
        yValues[3]  = (int) Config.context.getResources().getDimension(R.dimen.Gs6);

        //A
        yValues[38] = (int) Config.context.getResources().getDimension(R.dimen.A3);
        yValues[26] = (int) Config.context.getResources().getDimension(R.dimen.A4);
        yValues[14] = (int) Config.context.getResources().getDimension(R.dimen.A5);
        yValues[2]  = (int) Config.context.getResources().getDimension(R.dimen.A6);

        //Bb
        yValues[37] = (int) Config.context.getResources().getDimension(R.dimen.Bb3);
        yValues[25] = (int) Config.context.getResources().getDimension(R.dimen.Bb4);
        yValues[13] = (int) Config.context.getResources().getDimension(R.dimen.Bb5);
        yValues[1]  = (int) Config.context.getResources().getDimension(R.dimen.Bb6);

        //B
        yValues[36] = (int) Config.context.getResources().getDimension(R.dimen.B3);
        yValues[24] = (int) Config.context.getResources().getDimension(R.dimen.B4);
        yValues[12] = (int) Config.context.getResources().getDimension(R.dimen.B5);
        yValues[0]  = (int) Config.context.getResources().getDimension(R.dimen.B6);*/

    }


    public static int findYIndex(float valueToFind) {

        for (int i = 0; i < yValues.length; i++) {
            if (valueToFind == (float)yValues[i]) {
                return i;
            }
        }
        return -1;
    }
}
