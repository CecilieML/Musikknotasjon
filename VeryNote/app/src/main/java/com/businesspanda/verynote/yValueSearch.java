package com.businesspanda.verynote;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CecilieMarie on 11.03.2015.
 */
public class yValueSearch {

    public static int[] yValues = new int[48];

    public static void createYValues() {

        //C
        yValues[47] = (int) Config.context.getResources().getDimension(R.dimen.C3);
        yValues[35] = (int) Config.context.getResources().getDimension(R.dimen.C4);
        yValues[23] = (int) Config.context.getResources().getDimension(R.dimen.C5);
        yValues[11] = (int) Config.context.getResources().getDimension(R.dimen.C6);

        //C#
        yValues[46] = (int) Config.context.getResources().getDimension(R.dimen.Cs3);
        yValues[34] = (int) Config.context.getResources().getDimension(R.dimen.Cs4);
        yValues[22] = (int) Config.context.getResources().getDimension(R.dimen.Cs5);
        yValues[10] = (int) Config.context.getResources().getDimension(R.dimen.Cs6);

        //D
        yValues[45] = (int) Config.context.getResources().getDimension(R.dimen.D3);
        yValues[33] = (int) Config.context.getResources().getDimension(R.dimen.D4);
        yValues[21] = (int) Config.context.getResources().getDimension(R.dimen.D5);
        yValues[9]  = (int) Config.context.getResources().getDimension(R.dimen.D6);

        //Eb
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
        yValues[0]  = (int) Config.context.getResources().getDimension(R.dimen.B6);

    }


    public int findYValue(int valueToFind) {

        for (int i = 0; i < yValues.length; i++) {
            if (valueToFind == yValues[i]) {
                return i;
            }
        }
        return -1;
    }
}
