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

import java.util.ArrayList;


/************
 * http://www.shutterstock.com/cat.mhtml?pl=47643-42764&searchterm=panda
 *
 * panda picture site :D
 */

public class MyTouchListener implements View.OnTouchListener {

    boolean oneIsCurrentlyChosen;
    boolean upSideDownNote; //true = note is flipped
    ImageView lastImg;
    RelativeLayout really;
    Note noteObject;
    Button btnUp;
    Button btnDown;
    Button btnFlat;
    Button btnSharp;
    Button btnNeutral;
    Button btnRemoveAll;

    int shortVib = 50;
    int longVib = 100;
    int index;

    ArrayList<Note> allNotes;

    public MyTouchListener(RelativeLayout really, ArrayList<Note> allNotes) {
        this.really = really;
        this.allNotes = allNotes;
    }

    public void vibrate(int dur) {
        Vibrator vib = (Vibrator) Config.context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(dur);
    }

    public void fixLines(RelativeLayout parentLayout){

        for(int i=0;i<parentLayout.getChildCount();i++){
            View child = parentLayout.getChildAt(i);
            ImageView childView = (ImageView) child;
            String imgName = Config.context.getResources().getResourceEntryName(child.getId());
            if(imgName.length() >= 8 ){
                childView.setVisibility(View.GONE);
            }
        }

        Config.context.notesOutOfBoundsLines(noteObject.getNmbOfLinesTreble(), noteObject.getNmbOfLinesBass(), noteObject.getNoteHeight(), parentLayout);

        for(int i=0;i<parentLayout.getChildCount();i++){
            View child = parentLayout.getChildAt(i);
            ImageView childView = (ImageView) child;
            String imgName = Config.context.getResources().getResourceEntryName(child.getId());
            if(imgName.length() >= 8 ){
                ColorFilter filter = new LightingColorFilter(Color.CYAN, Color.CYAN);
                childView.setColorFilter(filter);
            }
        }

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

                findIndex(parentLayout);
                setNoteName(parentLayout, false, true);

                findIndex(parentLayout);
                fixLines(parentLayout);

                if(index<1)index = 1;
                float percent = FitToScreen.returnViewHeight(yValueSearch.yValues[index-1]);

                if(upSideDownNote) percent += FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.upSideDownNoteX));

                for(int i=0;i<parentLayout.getChildCount();i++) {
                    View child = parentLayout.getChildAt(i);
                    String childName = Config.context.getResources().getResourceEntryName(child.getId());
                    if(childName.length() <= 3) {
                        child.setY(percent);
                    }else if(childName.length() == 4){
                        //flat
                        if(upSideDownNote){
                            child.setY(percent - FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.flatOffsetY)));
                        }else {
                            child.setY(percent + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.flatOffsetY)));
                        }
                    }else if(childName.length() > 4&& childName.length() < 8){
                        //sharp and neutral
                        if(upSideDownNote) {
                            float noteHeight = FitToScreen.returnViewHeight(yValueSearch.yValues[index-1]);
                            child.setY(noteHeight + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                        }else{
                            child.setY(percent + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                        }
                    }
                }
                vibrate(shortVib);
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

                findIndex(parentLayout);
                setNoteName(parentLayout, true, false);

                findIndex(parentLayout);
                fixLines(parentLayout);

                if(index>26)index=26;
                float percent = FitToScreen.returnViewHeight(yValueSearch.yValues[index+1]);

                if(upSideDownNote) percent += FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.upSideDownNoteX));

                for(int i=0;i<parentLayout.getChildCount();i++) {
                    View child = parentLayout.getChildAt(i);
                    String childName = Config.context.getResources().getResourceEntryName(child.getId());
                    if(childName.length() <= 3) {
                        child.setY(percent);
                    }else if(childName.length() == 4){
                        //flat
                        if(upSideDownNote){
                            child.setY(percent - FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.flatOffsetY)));
                        }else {
                            child.setY(percent + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.flatOffsetY)));
                        }
                    }else if(childName.length() > 4&& childName.length() < 8){
                        //sharp and neutral
                        if(upSideDownNote) {
                            float noteHeight = FitToScreen.returnViewHeight(yValueSearch.yValues[index+1]);
                            child.setY(noteHeight + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                        }else{
                            child.setY(percent + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                        }
                    }
                }

                vibrate(shortVib);
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
                findIndex(parentLayout);

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

                    float noteHeight = FitToScreen.returnViewHeight(yValueSearch.yValues[index]);
                    flat.setY(noteHeight + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.flatOffsetY)));

                    parentLayout.addView(flat);
                }
                vibrate(shortVib);

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
                findIndex(parentLayout);

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

                    float noteHeight = FitToScreen.returnViewHeight(yValueSearch.yValues[index]);
                    sharp.setY(noteHeight + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));

                    parentLayout.addView(sharp);
                }

                vibrate(shortVib);
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
                findIndex(parentLayout);

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

                    float noteHeight = FitToScreen.returnViewHeight(yValueSearch.yValues[index]);
                    neutral.setY(noteHeight + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));

                    parentLayout.addView(neutral);
                }

                vibrate(shortVib);
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

                findNote:
                for(int i=0; i<parentLayout.getChildCount(); i++) {
                    View child = parentLayout.getChildAt(i);
                    String imgName = Config.context.getResources().getResourceEntryName(child.getId());
                    if (imgName.length() <= 3) {

                        int idx = (int)child.getTag();
                        Note oldNote = allNotes.get(idx);
                        Note replacementNote =  new Note(oldNote.isSharp(), oldNote.isFlat(),
                                oldNote.getFreq(), oldNote.getName(), oldNote.getNoteHeight() ,
                                oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(), "");
                        allNotes.set(idx, replacementNote);

                        break  findNote;
                    }
                }



                parentLayout.removeAllViews();

                TextView freqText = (TextView) Config.context.findViewById(R.id.freqTextview);
                freqText.setText("");

                removeButtons();
                v.setSelected(false);
                oneIsCurrentlyChosen = false;



                vibrate(longVib);
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

    public void findIndex(RelativeLayout parentLayout){
        String noteName = "";
        View note;
        int y = 0;
        for(int i=0;i<parentLayout.getChildCount();i++){
            View child = parentLayout.getChildAt(i);
            String imgName = Config.context.getResources().getResourceEntryName(child.getId());
            if(imgName.length() <= 3){
                note = child;
                noteName = imgName;
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

        if(upSideDownNote)y -= FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.upSideDownNoteX));
        index = yValueSearch.returnIdx(y - actionAndNotBarHeight);

        noteObject = NoteSearch.findNoteByName(noteName);
    }

    public void setNoteName(RelativeLayout parentLayout, boolean next, boolean prev){
        TextView freqText = (TextView) Config.context.findViewById(R.id.freqTextview);
        for(int i=0;i<parentLayout.getChildCount();i++){
            View child = parentLayout.getChildAt(i);
            String imgName = Config.context.getResources().getResourceEntryName(child.getId());
            if(imgName.length() <= 3){
                if(next) {
                    //down
                    if(index>26)index=26;
                    String newNoteName = NoteNameSearch.NoteNames[index+1];
                    int noteID = Config.context.getResources().getIdentifier(newNoteName, "dimen", Config.context.getPackageName());
                    child.setId(noteID);
                    imgName = newNoteName;
                }else if(prev){
                    //up
                    if(index<1)index=1;
                    String newNoteName = NoteNameSearch.NoteNames[index-1];
                    int noteID = Config.context.getResources().getIdentifier(newNoteName, "dimen", Config.context.getPackageName());
                    child.setId(noteID);
                    imgName = newNoteName;
                }

                imgName = imgName.replaceAll(""," ");
                imgName = imgName.replaceAll(" s","#");
                imgName = imgName.replaceAll(" b","b");
              // freqText.setText(imgName);


            }
        }

    }

    public void onChosenNote(View v){
        lastImg = (ImageView) v;
        vibrate(70);
        createButtons((ImageView)v);

        ColorFilter filter = new LightingColorFilter(Color.CYAN, Color.CYAN);

        RelativeLayout parentLayout = (RelativeLayout) v.getParent();

        upSideDownNote = isNoteUpSideDown(parentLayout);
        upSideDownNote = isNoteUpSideDown(parentLayout);

        for(int i=0;i<parentLayout.getChildCount();i++){
            View child = parentLayout.getChildAt(i);
            ImageView childView = (ImageView) child;
            childView.setColorFilter(filter);
        }

       findIndex(parentLayout);

        TextView freqText = (TextView) Config.context.findViewById(R.id.freqTextview);
        freqText.setText(" Tag:  " + v.getTag());

        setNoteName(parentLayout, false, false);
        for(int i = 0; i<allNotes.size(); i++) {
            System.out.println(allNotes.get(i).getDurationOfNote() + ";  -->  " + i + ", notename: " + allNotes.get(i).getName() + "\n");
        }
    }

    public void onUnChosenNote(){
        vibrate(30);
        removeButtons();
        lastImg.setSelected(false);

        RelativeLayout parentLayout = (RelativeLayout) lastImg.getParent();
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
