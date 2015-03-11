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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        btnUp.setY(Config.context.getResources().getDimension(R.dimen.btnUpY));
        btnUp.setX(Config.context.getResources().getDimension(R.dimen.btnUpX));
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();
                int y = (int) imgView.getY();
                int index = yValueSearch.findYValue(y);
                parentLayout.setY(yValueSearch.yValues[index-1]);
                System.out.println("original " + y + "  new value  " + yValueSearch.yValues[index-1]);
            }
        });

        btnDown = new Button(Config.context);
        btnDown.setText("DOWN");
        btnDown.setVisibility(View.VISIBLE);
        btnDown.setY(Config.context.getResources().getDimension(R.dimen.btnDownY));
        btnDown.setX(Config.context.getResources().getDimension(R.dimen.btnDownX));
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();
                int y = (int) imgView.getY();
                int index = yValueSearch.findYValue(y);
                parentLayout.setY(yValueSearch.yValues[index+1]);
                System.out.println("original " + y + "  new value  " + yValueSearch.yValues[index+1]);
            }
        });

        really.addView(btnUp);
        really.addView(btnDown);
    }

    Button btnUp;
    Button btnDown;

    public void removeButtons(){
        btnDown.setVisibility(View.GONE);
        btnUp.setVisibility(View.GONE);
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: {
                // Here u can write code which is executed after the user touch on the screen

                if (!oneIsCurrentlyChosen){
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


                    oneIsCurrentlyChosen = true;
                }else{
                    if(v==img) {
                        vibIy(30);
                        removeButtons();
                        v.setSelected(false);

                        RelativeLayout parentLayout = (RelativeLayout) img.getParent();
                        for(int i=0;i<parentLayout.getChildCount();i++){
                            View child = parentLayout.getChildAt(i);
                            ImageView childView = (ImageView) child;
                            childView.clearColorFilter();
                        }

                        oneIsCurrentlyChosen = false;
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
