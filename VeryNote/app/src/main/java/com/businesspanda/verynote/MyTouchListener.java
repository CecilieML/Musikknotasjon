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
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Vibrator;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/************
 * http://www.shutterstock.com/cat.mhtml?pl=47643-42764&searchterm=panda
 *
 * panda picture site :D
 */

public class MyTouchListener implements View.OnTouchListener {

    boolean oneIsCurrentlyChosen;
    boolean upSideDownNote; //true = note is flipped
    ImageView img;
    RelativeLayout really;
    Button btnUp;
    Button btnDown;
    Button btnFlat;
    Button btnSharp;
    Button btnNeutral;
    Button btnRemoveAll;

    int shortVib = 50;
    int longVib = 100;

    public MyTouchListener(RelativeLayout really) {
        this.really = really;
    }

    public void vibIy(int dur) {
        Vibrator vib = (Vibrator) Config.context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(dur);
    }

    public void removeLines(){

    }

    public void addLines(){

    }

    public boolean isNoteUpSideDown(RelativeLayout parentLayout){
        if(parentLayout.getId() == R.id.upsideDown){
            return true;
        }else {
            return false;
        }
    }

    public void createButtons(final ImageView imgView){

        FrameLayout.LayoutParams btnParams= new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnWidth)),
                FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnHeight)));

        btnUp = new Button(Config.context);
        btnUp.setText("UP");
        btnUp.setLayoutParams(btnParams);
        btnUp.setBackgroundResource(R.drawable.fancy_buttons);
        btnUp.setVisibility(View.VISIBLE);
        btnUp.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnUpY)));
        btnUp.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnUpX)));
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();
                View note;

                int y = 0;

                for(int i=0;i<parentLayout.getChildCount();i++){
                    View child = parentLayout.getChildAt(i);
                    String noteName = Config.context.getResources().getResourceEntryName(child.getId());
                    if(noteName.length() <= 3){
                        note = child;
                        int[] xyPos = new int[2];
                        note.getLocationOnScreen(xyPos);
                        y = xyPos[1];
                    }
                }

                View content = Config.context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                int height = content.getHeight();

                WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int fullHeight = display.getHeight();

                int actionAndNotBarHeight = fullHeight - height;

                /***/

                upSideDownNote = isNoteUpSideDown(parentLayout);
                if(upSideDownNote) y -= FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.upSideDownNoteX));


                /*int index = yValueSearch.findYIndex(y-actionAndNotBarHeight);
                System.out.println(index + "  index in mytouchlstener");
                if(index>0){}
                    float percent = FitToScreen.returnViewHeight(yValueSearch.yValues[index-1]);*/

                int index = yValueSearch.returnPrev(y - actionAndNotBarHeight);
                if(index<0)index=0;
                float percent = FitToScreen.returnViewHeight(yValueSearch.yValues[index]);

                if(upSideDownNote) percent += FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.upSideDownNoteX));

                    for(int i=0;i<parentLayout.getChildCount();i++) {
                        View child = parentLayout.getChildAt(i);
                        String childName = Config.context.getResources().getResourceEntryName(child.getId());
                        if(childName.length()<4) {
                            child.setY(percent);
                        }

                    }

                vibIy(shortVib);

            }
        });

        btnDown = new Button(Config.context);
        btnDown.setText("DOWN");
        btnDown.setLayoutParams(btnParams);
        btnDown.setBackgroundResource(R.drawable.fancy_buttons);
        btnDown.setVisibility(View.VISIBLE);
        btnDown.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnDownY)));
        btnDown.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnDownX)));
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();
                View note;

                int y = 0;

                for(int i=0;i<parentLayout.getChildCount();i++){
                    View child = parentLayout.getChildAt(i);
                    String noteName = Config.context.getResources().getResourceEntryName(child.getId());
                    if(noteName.length() <= 3){
                        note = child;
                        int[] xyPos = new int[2];
                        note.getLocationOnScreen(xyPos);
                        y = xyPos[1];
                    }
                }

                View content = Config.context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
                int height = content.getHeight();

                WindowManager wm = (WindowManager) Config.context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int fullHeight = display.getHeight();

                int actionAndNotBarHeight = fullHeight - height;

               /* int[] xyPos = new int[2];
                parentLayout.getLocationOnScreen(xyPos);
                int y = xyPos[1];*/

                upSideDownNote = isNoteUpSideDown(parentLayout);
                if(upSideDownNote) y -= FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.upSideDownNoteX));

                /*int index = yValueSearch.findYIndex(y-actionAndNotBarHeight);
                System.out.println("indrx-----  " + index);

                float percent = FitToScreen.returnViewHeight(yValueSearch.yValues[index+1]);*/

                int index = yValueSearch.returnNext(y - actionAndNotBarHeight);
                if(index>27)index=27;
                float percent = FitToScreen.returnViewHeight(yValueSearch.yValues[index]);

                System.out.println(index + " <-- index, percent --> " + percent + " DOWN --> " + yValueSearch.yValues[index]);

                if(upSideDownNote) percent += FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.upSideDownNoteX));

                for(int i=0;i<parentLayout.getChildCount();i++) {
                    View child = parentLayout.getChildAt(i);
                    String childName = Config.context.getResources().getResourceEntryName(child.getId());
                    if(childName.length()<4) {
                        child.setY(percent);
                    }else if(childName.length()>=4 && childName.length()<7){
                       // child.setY(percent);
                    }
                }

                vibIy(shortVib);
            }
        });

        btnFlat = new Button(Config.context);
        btnFlat.setText("b");
        btnFlat.setLayoutParams(btnParams);
        btnFlat.setBackgroundResource(R.drawable.fancy_buttons);
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
                vibIy(shortVib);

            }
        });

        btnSharp = new Button(Config.context);
        btnSharp.setText("#");
        btnSharp.setLayoutParams(btnParams);
        btnSharp.setBackgroundResource(R.drawable.fancy_buttons);
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

                vibIy(shortVib);
            }
        });

        btnNeutral = new Button(Config.context);
        btnNeutral.setText("n");

        btnNeutral.setLayoutParams(btnParams);

        btnNeutral.setBackgroundResource(R.drawable.fancy_buttons);
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

                vibIy(shortVib);
            }
        });

        btnRemoveAll = new Button(Config.context);
        btnRemoveAll.setText("Delete note");

        btnRemoveAll.setLayoutParams(btnParams);

        btnRemoveAll.setBackgroundResource(R.drawable.fancy_buttons);
        btnRemoveAll.setTextColor(Color.RED);
        btnRemoveAll.setVisibility(View.VISIBLE);
        btnRemoveAll.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnRemoveAllY)));
        btnRemoveAll.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnRemoveAllX)));
        btnRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();
                parentLayout.removeAllViews();

                TextView freqText = (TextView) Config.context.findViewById(R.id.freqTextview);
                freqText.setText("");

                removeButtons();
                v.setSelected(false);
                oneIsCurrentlyChosen = false;

                vibIy(longVib);
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

        TextView freqText = (TextView) Config.context.findViewById(R.id.freqTextview);
        for(int i=0;i<parentLayout.getChildCount();i++){
            View child = parentLayout.getChildAt(i);
            String noteName = Config.context.getResources().getResourceEntryName(child.getId());
            if(noteName.length() <= 3){
                noteName = noteName.replaceAll(""," ");
                noteName = noteName.replaceAll(" s","#");
                noteName = noteName.replaceAll(" b","b");
                freqText.setText(noteName);
            }
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
        TextView freqText = (TextView) Config.context.findViewById(R.id.freqTextview);
        freqText.setText("");
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                // Here u can write code which is executed after the user touch on the screen

                if (!oneIsCurrentlyChosen){
                    if(MainActivity.editable) {
                        onChosenNote(v);
                        oneIsCurrentlyChosen = true;
                    }

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
