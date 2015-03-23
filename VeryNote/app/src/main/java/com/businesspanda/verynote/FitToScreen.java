package com.businesspanda.verynote;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Helene on 13.03.2015.
 */
public class FitToScreen {

    public static int returnViewHeight(double percentOfScreen) {

        View content = Config.context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int height = content.getHeight();

        return (int)(height * percentOfScreen);
    }

    public static int returnViewWidth(double percentOfScreen) {
        WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        return (int)(width * percentOfScreen);
    }

    public static float returnPercent(float yValue){
        View content = Config.context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int height = content.getHeight();

        float percentValue = yValue/height;

        //System.out.println(percentValue + " & = " + yValue + " / " + height);

        return percentValue;
    }

}
