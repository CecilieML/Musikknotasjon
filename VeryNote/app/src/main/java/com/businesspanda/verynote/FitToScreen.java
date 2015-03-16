package com.businesspanda.verynote;

import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

    public static int returnViewHeight(View view, double percentOfScreenCovering) {
        WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        return (int)(height * percentOfScreenCovering);
    }

    public static int returnViewWidth(View view, double percentOfScreenCovering) {
        WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        return (int)(height * percentOfScreenCovering);
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
