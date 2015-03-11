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
        yValues[0] = (int) Config.context.getResources().getDimension(R.dimen.C3);
        yValues[1] = (int) Config.context.getResources().getDimension(R.dimen.C4);
        yValues[2] = (int) Config.context.getResources().getDimension(R.dimen.C5);
        yValues[3] = (int) Config.context.getResources().getDimension(R.dimen.C6);

        //C#
        yValues[4] = (int) Config.context.getResources().getDimension(R.dimen.Cs3);
        yValues[5] = (int) Config.context.getResources().getDimension(R.dimen.Cs4);
        yValues[6] = (int) Config.context.getResources().getDimension(R.dimen.Cs5);
        yValues[7] = (int) Config.context.getResources().getDimension(R.dimen.Cs6);

        System.out.println("value is  " + yValues[0] + "  it should be 100");
        System.out.println("value is  " + yValues[1] + "  it should be 35");
        System.out.println("value is  " + yValues[2] + "  it should be 53");
        System.out.println("value is  " + yValues[3] + "  it should be 11");

        System.out.println("this is 100 --->  "+Config.context.getResources().getDimension(R.dimen.C3));
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
