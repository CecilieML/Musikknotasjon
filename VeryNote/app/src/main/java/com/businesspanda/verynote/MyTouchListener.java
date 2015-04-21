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


//A class concerning what happens when you touch a note after recording
public class MyTouchListener implements View.OnTouchListener {

    ArrayList<Note> allNotes;

    Note noteObject;
    ImageView lastImg;
    RelativeLayout really;
    RelativeLayout fullNoteLayout;

    Button btnUp;
    Button btnDown;
    Button btnFlat;
    Button btnSharp;
    Button btnNeutral;
    Button btnRemoveAll;

    int shortVib = 50;
    int longVib = 100;
    int index;

    boolean oneIsCurrentlyChosen;
    boolean upSideDownNote; //true = note is flipped

    public MyTouchListener(RelativeLayout really, ArrayList<Note> allNotes) {
        this.really = really;
        this.allNotes = allNotes;
    }

    //Creates the buttons and their functionality
    public void createButtons(final ImageView imgView){

        fullNoteLayout = (RelativeLayout) imgView.getParent();

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

                        int idx = (int) child.getTag();
                        Note oldNote = allNotes.get(idx);
                        Note replacementNote = new Note(false, false, false,
                                oldNote.getFreq(), childName, oldNote.getNoteHeight(),
                                oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(),
                                oldNote.getDurationOfNote());

                        fixName(parentLayout, replacementNote);
                        allNotes.set(idx, replacementNote);

                    }else if( child.getId() == R.id.neutral){
                        if(upSideDownNote) {
                            float noteHeight = FitToScreen.returnViewHeight(yValueSearch.yValues[index-1]);
                            child.setY(noteHeight + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                        }else{
                            child.setY(percent + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                        }

                    }else if(child.getId() == R.id.flat || child.getId() == R.id.sharp){
                        parentLayout.removeView(child);
                    }

                }
                setAbleButtons(btnFlat, "b");
                setAbleButtons(btnSharp, "s");
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

                        int idx = (int) child.getTag();
                        Note oldNote = allNotes.get(idx);
                        Note replacementNote = new Note(false, false, false,
                                oldNote.getFreq(), childName, oldNote.getNoteHeight(),
                                oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(),
                                oldNote.getDurationOfNote());

                        fixName(parentLayout, replacementNote);
                        allNotes.set(idx, replacementNote);

                    }else if(child.getId() == R.id.neutral){
                        if(upSideDownNote) {
                            float noteHeight = FitToScreen.returnViewHeight(yValueSearch.yValues[index-1]);
                            child.setY(noteHeight + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                        }else{
                            child.setY(percent + FitToScreen.returnViewHeight(MainActivity.getPercent(R.dimen.sharpOffsetY)));
                        }

                    }else if(child.getId() == R.id.flat || child.getId() == R.id.sharp){
                        parentLayout.removeView(child);
                    }
                }
                setAbleButtons(btnFlat, "b");
                setAbleButtons(btnSharp, "s");
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
        btnFlat.setTransformationMethod(null);

        setAbleButtons(btnFlat, "b");

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
                        String imgName = Config.context.getResources().getResourceEntryName(child.getId());
                        if(child.getId() == R.id.flat){
                            parentLayout.removeView(child);
                        }
                        if (imgName.length() <= 3) {
                            int idx = (int) child.getTag();
                            Note oldNote = allNotes.get(idx);
                            Note replacementNote = new Note(false, false, false,
                                    oldNote.getFreq(), oldNote.getName(), oldNote.getNoteHeight(),
                                    oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(), oldNote.getDurationOfNote());
                            fixName(parentLayout, replacementNote);
                            allNotes.set(idx, replacementNote);
                        }
                    }
                }else {
                        //if (children > 1) {
                            for (int i = 0; i < parentLayout.getChildCount(); i++) {
                                View child = parentLayout.getChildAt(i);
                                String imgName = Config.context.getResources().getResourceEntryName(child.getId());
                                if (child.getId() == R.id.sharp) {
                                    parentLayout.removeView(child);
                                }
                                if (child.getId() == R.id.neutral) {
                                    parentLayout.removeView(child);
                                }
                                if (imgName.length() <= 3) {
                                    int idx = (int) child.getTag();
                                    Note oldNote = allNotes.get(idx);
                                    Note replacementNote = new Note(false, true, false,
                                            oldNote.getFreq(), oldNote.getName(), oldNote.getNoteHeight(),
                                            oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(), oldNote.getDurationOfNote());
                                    fixName(parentLayout, replacementNote);
                                    allNotes.set(idx, replacementNote);
                                }
                            }
                       // }

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

        setAbleButtons(btnSharp, "s");

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
                        String imgName = Config.context.getResources().getResourceEntryName(child.getId());
                        if(child.getId() == R.id.sharp){
                            parentLayout.removeView(child);
                        }
                        if (imgName.length() <= 3) {
                            int idx = (int) child.getTag();
                            Note oldNote = allNotes.get(idx);
                            Note replacementNote = new Note(false, false, false,
                                    oldNote.getFreq(), oldNote.getName(), oldNote.getNoteHeight(),
                                    oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(), oldNote.getDurationOfNote());
                            fixName(parentLayout, replacementNote);
                            allNotes.set(idx, replacementNote);
                        }
                    }
                }else {
                     //   if (children > 1) {
                            for (int i = 0; i < parentLayout.getChildCount(); i++) {
                                View child = parentLayout.getChildAt(i);
                                String imgName = Config.context.getResources().getResourceEntryName(child.getId());
                                if (child.getId() == R.id.flat) {
                                    parentLayout.removeView(child);
                                }
                                if (child.getId() == R.id.neutral) {
                                    parentLayout.removeView(child);
                                }
                                if (imgName.length() <= 3) {
                                    int idx = (int) child.getTag();
                                    Note oldNote = allNotes.get(idx);
                                    Note replacementNote = new Note(true, false, false, oldNote.getFreq(),
                                            oldNote.getName(), oldNote.getNoteHeight(),
                                            oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(),
                                            oldNote.getDurationOfNote());
                                    fixName(parentLayout, replacementNote);
                                    allNotes.set(idx, replacementNote);
                                }
                            }
                       // }

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
        btnNeutral.setTransformationMethod(null);
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
                        String imgName = Config.context.getResources().getResourceEntryName(child.getId());
                        if(child.getId() == R.id.neutral){
                            parentLayout.removeView(child);
                        }
                        if (imgName.length() <= 3) {
                            int idx = (int) child.getTag();
                            Note oldNote = allNotes.get(idx);
                            Note replacementNote = new Note(false, false, false,
                                    oldNote.getFreq(), oldNote.getName(), oldNote.getNoteHeight(),
                                    oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(), oldNote.getDurationOfNote());
                            fixName(parentLayout, replacementNote);
                            allNotes.set(idx, replacementNote);
                        }
                    }
                }else{
                  //  if(children>1){
                        for(int i=0;i<parentLayout.getChildCount();i++){
                            View child = parentLayout.getChildAt(i);
                            String imgName = Config.context.getResources().getResourceEntryName(child.getId());
                            if(child.getId() == R.id.flat) {
                                parentLayout.removeView(child);
                            }
                            if(child.getId() == R.id.sharp) {
                                parentLayout.removeView(child);
                                System.out.println("DID ONLY THIS FOR SOME REASON!!!!");
                            }
                            if (imgName.length() <= 3) {
                                int idx = (int) child.getTag();
                                Note oldNote = allNotes.get(idx);
                                Note replacementNote = new Note(false, false, true, oldNote.getFreq(),
                                        oldNote.getName(), oldNote.getNoteHeight(),
                                        oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(),
                                        oldNote.getDurationOfNote());
                                fixName(parentLayout, replacementNote);
                                System.out.println("WHHHHHHHHHHHHHHHHAAAAAAAAAAAAAAAT!!!!!??????????");
                                allNotes.set(idx, replacementNote);
                            }
                        }
                   // }

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
                        Note replacementNote =  new Note(oldNote.isSharp(), oldNote.isFlat(), oldNote.isNeutral(),
                                oldNote.getFreq(), oldNote.getName(), oldNote.getNoteHeight() ,
                                oldNote.getNmbOfLinesTreble(), oldNote.getNmbOfLinesBass(), "");
                        allNotes.set(idx, replacementNote);

                        break  findNote;
                    }
                }

                parentLayout.removeAllViews();

                /**TextView freqText = (TextView) Config.context.findViewById(R.id.freqTextview);
                freqText.setText("");*/

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

    //Removes buttons from UI
    public void removeButtons(){
        btnDown.setVisibility(View.GONE);
        btnUp.setVisibility(View.GONE);
        btnFlat.setVisibility(View.GONE);
        btnSharp.setVisibility(View.GONE);
        btnNeutral.setVisibility(View.GONE);
        btnRemoveAll.setVisibility(View.GONE);
    }

    //Checks to see if there exist an id for this note, if the is not the button is disabled
    public void setAbleButtons(Button button, String x) {
        int nameID = 0;
        for (int i = 0; i < fullNoteLayout.getChildCount(); i++) {
            View child = fullNoteLayout.getChildAt(i);
            String imgName = Config.context.getResources().getResourceEntryName(child.getId());
            if (imgName.length() <= 3) {
                String root = imgName.substring(0, 1);
                String octave = imgName.substring(imgName.length() - 1, imgName.length());

                String fullName = root + x + octave;
                nameID = Config.context.getResources().getIdentifier(fullName, "dimen", Config.context.getPackageName());
            }
        }
        if (nameID == 0) {
            button.setEnabled(false);
        }else {
            button.setEnabled(true);
        }
    }

    //Called to make phone vibrate and let user know that their button press registered
    public void vibrate(int dur) {
        Vibrator vib = (Vibrator) Config.context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(dur);
    }

    //Deletes all "note lines" and puts in the correct number of new ones
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

    //Checks of note is flipped
    public boolean isNoteUpSideDown(RelativeLayout parentLayout){
        if(parentLayout.getId() == R.id.upsideDown){
            return true;
        }else {
            return false;
        }
    }

    // Removes the "b"/"s" from the notes name
    // of adds the "b"/"s" to the notes name
    public void fixName(RelativeLayout parentLayout, Note note){
        System.out.println(note.isNeutral() + "  <--NEUTRAL");
        for(int i=0;i<parentLayout.getChildCount();i++) {
            View child = parentLayout.getChildAt(i);
            String imgName = Config.context.getResources().getResourceEntryName(child.getId());
            if(imgName.length() == 3){

                System.out.println("1FABLAGURGH  " + imgName);

                String noteName = note.getName();

                String root = noteName.substring(0, 1);
                String octave = noteName.substring(2, 3);

                String fullName = root + octave;
                note.setName(fullName);

                int nameID = Config.context.getResources().getIdentifier(fullName, "dimen", Config.context.getPackageName());
                child.setId(nameID);

                imgName = Config.context.getResources().getResourceEntryName(child.getId());
                System.out.println("2AFTERRGRTY  " + fullName  + "   ID: " + Config.context.getResources().getResourceEntryName(child.getId()));

            }

            if(imgName.length() == 2){
                String noteName = note.getName();

                if (note.isFlat()) {
                    String root = noteName.substring(0, 1);
                    String octave = noteName.substring(1, 2);

                    String fullName = root + "b" + octave;
                    note.setName(fullName);

                    int nameID = Config.context.getResources().getIdentifier(fullName, "dimen", Config.context.getPackageName());
                    child.setId(nameID);

                }else if (note.isSharp()) {
                    System.out.println("3NATRUBEROR  " + imgName);
                    String root = noteName.substring(0, 1);
                    String octave = noteName.substring(1, 2);

                    String fullName = root + "s" + octave;
                    note.setName(fullName);

                    int nameID = Config.context.getResources().getIdentifier(fullName, "dimen", Config.context.getPackageName());
                    child.setId(nameID);
                    System.out.println("3NATRUBEROR  " + fullName + "   ID: " + Config.context.getResources().getResourceEntryName(child.getId()));

                }else if(note.isNeutral()) {
                    System.out.println("3NATRUBEROR  " + imgName);
                    String root = noteName.substring(0, 1);
                    String octave = noteName.substring(1, 2);

                    String fullName = root + "n" + octave;
                    note.setName(fullName);

                   int nameID = Config.context.getResources().getIdentifier(fullName, "dimen", Config.context.getPackageName());
                   //child.setId(nameID);

                   System.out.println("4afternartu  " + fullName + "  ID: " + nameID + "  MORESTUFF  " + R.id.Cn3);
                }
            }
        }
    }

    // Finds the notes index in the array containing the note names
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

    // Returns either the next or the previous name from the note name array
    public void setNoteName(RelativeLayout parentLayout, boolean next, boolean prev){
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
                }else if(prev){
                    //up
                    if(index<1)index=1;
                    String newNoteName = NoteNameSearch.NoteNames[index-1];
                    int noteID = Config.context.getResources().getIdentifier(newNoteName, "dimen", Config.context.getPackageName());
                    child.setId(noteID);
                }
            }
        }

    }

    // Called when a note is chosen
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

       setNoteName(parentLayout, false, false);
    }

    // Called on previous note when a new note is chosen or on current note when it is unselected
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

    }

    // Called when note is touched
    public boolean onTouch(View v, MotionEvent event)
    {
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
