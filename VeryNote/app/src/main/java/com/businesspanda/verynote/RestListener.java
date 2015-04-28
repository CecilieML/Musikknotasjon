package com.businesspanda.verynote;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by CecilieMarie on 28.04.2015.
 */
public class RestListener implements View.OnTouchListener {

    Button btnRemove;
    ImageView lastImg;
    RelativeLayout really;
    ArrayList<Note> allNotes;
    boolean oneIsCurrentlyChosen;

    public RestListener(RelativeLayout really, ArrayList<Note> allNotes) {
        this.really = really;
        this.allNotes = allNotes;
    }

    //Creates the buttons and their functionality
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

                removeButton();
                v.setSelected(false);

                oneIsCurrentlyChosen = false;

                vibrate(70);
            }
        });

        really.addView(btnRemove);
    }

    public void removeButton(){
        btnRemove.setVisibility(View.GONE);
    }

    //Called to make phone vibrate and let user know that their button press registered
    public void vibrate(int dur) {
        Vibrator vib = (Vibrator) Config.context.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(dur);
    }
    // Called when a note is chosen
    public void onChosenNote(View v){
        vibrate(70);
        lastImg = (ImageView) v;
        createButton((ImageView)v);

        ColorFilter filter = new LightingColorFilter(Color.CYAN, Color.CYAN);
        lastImg.setColorFilter(filter);
    }

    // Called on previous note when a new note is chosen or on current note when it is unselected
    public void onUnChosenNote(){
        vibrate(30);
        removeButton();
        lastImg.setSelected(false);
        lastImg.clearColorFilter();
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



