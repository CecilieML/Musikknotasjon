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
        btnUp.setY(Config.context.getResources().getDimension(R.dimen.btnUpY));
        btnUp.setX(Config.context.getResources().getDimension(R.dimen.btnUpX));
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout parentLayout = (RelativeLayout) imgView.getParent();
               /* int[] xyPos = new int[2];
                parentLayout.getLocationOnScreen(xyPos);
                int y = xyPos[1];
                int index = yValueSearch.findYIndex(y);
                System.out.println(index + "  uppp");
                if(index>0)
                    parentLayout.setY(yValueSearch.yValues[index-1]);
                System.out.println("original " + y + "  new value  " + yValueSearch.yValues[3]);*/
                parentLayout.setY(parentLayout.getY()-5);
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
               /* int[] xyPos = new int[2];
                parentLayout.getLocationInWindow(xyPos);

                //mainA = new MainActivity();
                int y = xyPos[1]-146;


                int index = yValueSearch.findYIndex(y);
                System.out.println(index + "  dowwwwn");
                if(index>=46 || index<0)index=46;
                parentLayout.setY(yValueSearch.yValues[index+1]);
                System.out.println(" searching for: "+ y +" new value  " + yValueSearch.yValues[index+1]);*/
                parentLayout.setY(parentLayout.getY()+5);

/*
I have found a solution.
1st I will get display height:
WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
Display display = wm.getDefaultDisplay();
int height = display.getHeight();
 and then set the height of llImages
LayoutParams params = llImages.getLayoutParams();
params.height = (int) (getDisplayHeight() * 0.66);

 */

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
