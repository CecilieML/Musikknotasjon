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

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FitToScreen {

    // Returns a pixel value based on percent of screen height
    public static int returnViewHeight(double percentOfScreen) {
        View content = Config.context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int height = content.getHeight();

        return (int)(height * percentOfScreen);
    }

    // Returns a pixel value based on percent of screen width
    public static int returnViewWidth(double percentOfScreen) {
        WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();

        return (int)(width * percentOfScreen);
    }

    // Returns the percent of the screen height that the pixel value it get in represents
    public static float returnPercent(float yValue){
        View content = Config.context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int height = content.getHeight();
        float percentValue = yValue/height;

        return percentValue;
    }

}
