package com.businesspanda.verynote;

/** Copyright (C) 2009 by Aleksey Surkov.
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
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;


public class RestListener implements View.OnTouchListener {

    Button btnRemove;
    ImageView lastImg;
    RelativeLayout really;
    ArrayList<Note> allNotes;
    boolean oneIsCurrentlyChosen;
    public static boolean restActive;

    public RestListener(RelativeLayout really, ArrayList<Note> allNotes) {
        this.really = really;
        this.allNotes = allNotes;
    }

    // Creates the buttons and their functionality
    public void createButton(final ImageView imgView){
        btnRemove = new Button(Config.context);
        btnRemove.setText("Delete rest");

        FrameLayout.LayoutParams btnParams= new FrameLayout.LayoutParams(
                FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnWidth)),
                FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnHeight)));
        btnRemove.setLayoutParams(btnParams);

        btnRemove.setBackgroundResource(R.drawable.fancy_buttons);
        btnRemove.setTextColor(Color.RED);
        btnRemove.setVisibility(View.VISIBLE);
        btnRemove.setY(FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.btnRemoveAllY)));
        btnRemove.setX(FitToScreen.returnViewWidth(MainActivity.getPercent(R.dimen.btnRemoveAllX)));
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgView.setVisibility(View.GONE);
                int idx = (int)imgView.getTag();

                Note replacementNote =  new Note(false, false, false, 0, "R", 0 , 0, 0, "");
                allNotes.set(idx, replacementNote);

                removeButton();
                v.setSelected(false);
                oneIsCurrentlyChosen = false;

                vibrate(70);
            }
        });

        really.addView(btnRemove);
    }

    // Removes button from UI
    public void removeButton(){
        btnRemove.setVisibility(View.GONE);
    }

    // Called to make phone vibrate and let user know that their button press registered
    public void vibrate(int dur) {
        Vibrator vib = (Vibrator) Config.context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(dur);
    }

    // Called when a rest is chosen
    public void onChosenNote(View v){
        if(Config.context.heyListen.touchActive){
            Config.context.heyListen.onUnChosenNote();
        }
        restActive = true;
        vibrate(70);
        lastImg = (ImageView) v;
        v.setBackgroundColor(Color.CYAN);
        createButton((ImageView)v);
    }

    // Called on previous rest when a new rest or note is chosen or on current rest when it is unselected
    public void onUnChosenNote(){
        vibrate(30);
        removeButton();
        lastImg.setSelected(false);
        lastImg.setBackgroundColor(Color.BLACK);
        restActive = false;
        oneIsCurrentlyChosen = false;
    }

    // Called when rest is touched
    public boolean onTouch(View v, MotionEvent event){

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                if (!oneIsCurrentlyChosen){
                    if(MainActivity.editable) {
                        onChosenNote(v);
                        oneIsCurrentlyChosen = true;
                    }
                }else{
                    if(v==lastImg) {
                        onUnChosenNote();
                        oneIsCurrentlyChosen = false;
                    }else {
                        onUnChosenNote();
                        onChosenNote(v);
                        oneIsCurrentlyChosen = true;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                break;
            }
        }
        return true;
    }

}



