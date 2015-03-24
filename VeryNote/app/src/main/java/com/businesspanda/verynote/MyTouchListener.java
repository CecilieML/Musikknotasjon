package com.businesspanda.verynote;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.location.LocationManager;
import android.media.Image;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Helene on 03.03.2015.
 */

/************
 * http://www.shutterstock.com/cat.mhtml?pl=47643-42764&searchterm=panda
 *
 * panda picture site :D
 */

public class MyTouchListener implements View.OnTouchListener {

    boolean oneIsCurrentlyChosen;
    ImageView img;
    RelativeLayout really;
    Button btnUp;
    Button btnDown;
    Button btnFlat;
    Button btnSharp;
    Button btnNeutral;
    Button btnRemoveAll;

    public MainActivity mainA;

    public MyTouchListener(RelativeLayout really) {
        this.really = really;
    }

    public void vibIy(int dur) {
        Vibrator vib = (Vibrator) Config.context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(dur);
    }

    public void createButtons(final ImageView imgView){
        btnUp = new Button(Config.context);
        btnUp.setText("UP");
        btnUp.setVisibility(View.VISIBLE);
        btnUp.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnUpY)));
        btnUp.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnUpX)));
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();

                View content = Config.context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                int height = content.getHeight();

                WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int fullHeight = display.getHeight();

                int actionAndNotBarHeight = fullHeight - height;

                int[] xyPos = new int[2];
                parentLayout.getLocationOnScreen(xyPos);
                int y = xyPos[1];
                int index = yValueSearch.findYIndex(y-actionAndNotBarHeight);
                if(index>0){
                    float percent = FitToScreen.returnViewHeight(yValueSearch.yValues[index-1]);
                    parentLayout.setY(percent);
                }

            }
        });

        btnDown = new Button(Config.context);
        btnDown.setText("DOWN");
        btnDown.setVisibility(View.VISIBLE);
        btnDown.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnDownY)));
        btnDown.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnDownX)));
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();

                View content = Config.context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                int height = content.getHeight();

                WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int fullHeight = display.getHeight();

                int actionAndNotBarHeight = fullHeight - height;

                int[] xyPos = new int[2];
                parentLayout.getLocationOnScreen(xyPos);
                int y = xyPos[1];
                int index = yValueSearch.findYIndex(y-actionAndNotBarHeight);
              //  System.out.println("final index =  " + index);
                if(index>0 && index<yValueSearch.yValues.length) {
                    float percent = FitToScreen.returnViewHeight(yValueSearch.yValues[index + 1]);
                    parentLayout.setY(percent);
                }

            }
        });

        btnFlat = new Button(Config.context);
        btnFlat.setText("b");
        btnFlat.setVisibility(View.VISIBLE);
        btnFlat.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnFlatY)));
        btnFlat.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnFlatX)));
        btnFlat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();

                boolean noteIsFlat = false;

                int children = parentLayout.getChildCount();

                for(int i=0;i<parentLayout.getChildCount();i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child.getId() == R.id.flat) {
                        noteIsFlat=true;
                    }
                }

                if(noteIsFlat){
                    for(int i=0;i<parentLayout.getChildCount();i++){
                        View child = parentLayout.getChildAt(i);
                        if(child.getId() == R.id.flat){
                            parentLayout.removeView(child);
                        }
                    }
                }else{
                    if(children>1){
                        for(int i=0;i<parentLayout.getChildCount();i++){
                            View child = parentLayout.getChildAt(i);
                            if(child.getId() == R.id.sharp) {
                                parentLayout.removeView(child);
                            }
                            if(child.getId() == R.id.neutral) {
                                parentLayout.removeView(child);
                            }
                        }
                    }

                    ImageView flat = new ImageView(Config.context);
                    flat.setImageResource(R.drawable.flatnotenew);
                    flat.setId(R.id.flat);
                    FrameLayout.LayoutParams paraFlat = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.flatHeight)));
                    flat.setLayoutParams(paraFlat);

                    ColorFilter filter = new LightingColorFilter(Color.CYAN, Color.CYAN);
                    flat.setColorFilter(filter);

                    flat.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.flatOffsetY)));
                    parentLayout.addView(flat);
                }

            }
        });

        btnSharp = new Button(Config.context);
        btnSharp.setText("#");
        btnSharp.setVisibility(View.VISIBLE);
        btnSharp.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnSharpY)));
        btnSharp.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnSharpX)));
        btnSharp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();

                boolean noteIsSharp = false;

                int children = parentLayout.getChildCount();

                for(int i=0;i<parentLayout.getChildCount();i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child.getId() == R.id.sharp) {
                        noteIsSharp=true;
                    }
                }

                if(noteIsSharp){
                    for(int i=0;i<parentLayout.getChildCount();i++){
                        View child = parentLayout.getChildAt(i);
                        if(child.getId() == R.id.sharp){
                            parentLayout.removeView(child);
                        }
                    }
                }else{
                    if(children>1){
                        for(int i=0;i<parentLayout.getChildCount();i++){
                            View child = parentLayout.getChildAt(i);
                            if(child.getId() == R.id.flat) {
                                parentLayout.removeView(child);
                            }
                            if(child.getId() == R.id.neutral) {
                                parentLayout.removeView(child);
                            }
                        }
                    }

                    ImageView sharp = new ImageView(Config.context);
                    sharp.setImageResource(R.drawable.sharpnotenew);
                    sharp.setId(R.id.sharp);
                    FrameLayout.LayoutParams paraSharp = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpHeight)));
                    sharp.setLayoutParams(paraSharp);

                    ColorFilter filter = new LightingColorFilter(Color.CYAN, Color.CYAN);
                    sharp.setColorFilter(filter);

                    sharp.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                    parentLayout.addView(sharp);
                }

            }
        });

        btnNeutral = new Button(Config.context);
        btnNeutral.setText("n");
        btnNeutral.setVisibility(View.VISIBLE);
        btnNeutral.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnNeutralY)));
        btnNeutral.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnNeutralX)));
        btnNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();

                boolean noteIsNeutral = false;

                int children = parentLayout.getChildCount();

                for(int i=0;i<parentLayout.getChildCount();i++) {
                    View child = parentLayout.getChildAt(i);
                    if (child.getId() == R.id.neutral) {
                        noteIsNeutral=true;
                    }
                }

                if(noteIsNeutral){
                    for(int i=0;i<parentLayout.getChildCount();i++){
                        View child = parentLayout.getChildAt(i);
                        if(child.getId() == R.id.neutral){
                            parentLayout.removeView(child);
                        }
                    }
                }else{
                    if(children>1){
                        for(int i=0;i<parentLayout.getChildCount();i++){
                            View child = parentLayout.getChildAt(i);
                            if(child.getId() == R.id.flat) {
                                parentLayout.removeView(child);
                            }
                            if(child.getId() == R.id.sharp) {
                                parentLayout.removeView(child);
                            }
                        }
                    }

                    ImageView neutral = new ImageView(Config.context);
                    neutral.setImageResource(R.drawable.naturalnote);
                    neutral.setId(R.id.neutral);
                    FrameLayout.LayoutParams paraNeutral = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpHeight)));
                    neutral.setLayoutParams(paraNeutral);

                    ColorFilter filter = new LightingColorFilter(Color.CYAN, Color.CYAN);
                    neutral.setColorFilter(filter);

                    neutral.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                    parentLayout.addView(neutral);
                }

            }
        });

        btnRemoveAll = new Button(Config.context);
        btnRemoveAll.setText("X");
        btnRemoveAll.setTextColor(Color.RED);
        btnRemoveAll.setVisibility(View.VISIBLE);
        btnRemoveAll.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnRemoveAllY)));
        btnRemoveAll.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnRemoveAllX)));
        btnRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();
                parentLayout.removeAllViews();

                removeButtons();
                v.setSelected(false);
                oneIsCurrentlyChosen = false;
            }
        });

        really.addView(btnUp);
        really.addView(btnDown);
        really.addView(btnFlat);
        really.addView(btnSharp);
        really.addView(btnNeutral);
        really.addView(btnRemoveAll);
    }

    public void removeButtons(){
        btnDown.setVisibility(View.GONE);
        btnUp.setVisibility(View.GONE);
        btnFlat.setVisibility(View.GONE);
        btnSharp.setVisibility(View.GONE);
        btnNeutral.setVisibility(View.GONE);
        btnRemoveAll.setVisibility(View.GONE);
    }

    public void onChosenNote(View v){
        img = (ImageView) v;
        vibIy(70);
        createButtons(img);

        ColorFilter filter = new LightingColorFilter(Color.CYAN, Color.CYAN);

        RelativeLayout parentLayout = (RelativeLayout) img.getParent();
        for(int i=0;i<parentLayout.getChildCount();i++){
            View child = parentLayout.getChildAt(i);
            ImageView childView = (ImageView) child;
            childView.setColorFilter(filter);
        }
    }

    public void onUnChosenNote(View v){
        vibIy(30);
        removeButtons();
        v.setSelected(false);

        RelativeLayout parentLayout = (RelativeLayout) v.getParent();
        for(int i=0;i<parentLayout.getChildCount();i++){
            View child = parentLayout.getChildAt(i);
            ImageView childView = (ImageView) child;
            childView.clearColorFilter();
        }
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                // Here u can write code which is executed after the user touch on the screen

                if (!oneIsCurrentlyChosen){

                    onChosenNote(v);

                    oneIsCurrentlyChosen = true;
                }else{
                    if(v==img) {

                        onUnChosenNote(img);

                        oneIsCurrentlyChosen = false;
                    }else {
                        onUnChosenNote(img);
                        onChosenNote(v);
                        oneIsCurrentlyChosen = true;
                    }
                }


                break;
            }
            case MotionEvent.ACTION_UP:
            {
                // Here u can write code which is executed after the user release the touch on the screen
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                // Here u can write code which is executed when user move the finger on the screen
                break;
            }
        }
        return true;
    }

}
