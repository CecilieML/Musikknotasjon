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

    public static void setViewHeight(View view, double percentOfScreenCovering) {
        WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int)(height * percentOfScreenCovering);
    }

    public static void setViewWidth(View view, double percentOfScreenCovering) {
        WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int)(height * percentOfScreenCovering);
    }

    public static int returnViewHeight(double percentOfScreen) {
       // WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        //Display display = wm.getDefaultDisplay();
        //int height = display.getHeight();
        //DisplayMetrics displayMetrics = new DisplayMetrics();
       // Config.context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

         //int height = displayMetrics.heightPixels;

        //ViewGroup.LayoutParams params = view.getLayoutParams();

        View content = Config.context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        Log.d("DISPLAY", content.getWidth() + " x " + content.getHeight());

        int height = content.getHeight();

        return (int)(height * percentOfScreen);
    }

    public static int returnViewWidth(double percentOfScreen) {
        WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        return (int)(width * percentOfScreen);
    }

    /*public static void setRelativeLayoutHeight(RelativeLayout relativeLayout, double percentOfScreenCovering) {
        WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        RelativeLayout.LayoutParams params = relativeLayout.getLayoutParams();
       // RelativeLayout.LayoutParams params = relativeLayout.getLayoutParams();
        params.height = (int)(height * percentOfScreenCovering);
    }

    public static void setRelativeLayoutWidth(RelativeLayout relativeLayout, double percentOfScreenCovering) {
        WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        RelativeLayout.LayoutParams params = relativeLayout.getLayoutParams();
        params.width = (int)(height * percentOfScreenCovering);
    }*/


}
