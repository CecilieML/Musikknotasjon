package com.businesspanda.verynote;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.location.LocationManager;
import android.media.Image;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
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

    public void gradient(RelativeLayout rellay){
        ImageView gradient = new ImageView(Config.context);
        ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient lg = new LinearGradient(0, 0, 0, img.getHeight(),
                        new int[]{
                                Color.GREEN,
                                Color.WHITE,
                                Color.BLUE,
                                Color.RED}, //substitute the correct colors for these
                        new float[]{
                                0, 0.45f, 0.55f, 1},
                        Shader.TileMode.REPEAT);
                return lg;
            }
        };
        PaintDrawable p = new PaintDrawable();
        p.setShaderFactory(sf);
        gradient.setBackgroundDrawable((Drawable)p);
        rellay.addView(gradient);
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
                parentLayout.setY(parentLayout.getY()-20);
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
                parentLayout.setY(parentLayout.getY()+20);
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
                    v.setSelected(true);
                    RelativeLayout parentLayout = (RelativeLayout) img.getParent();
                    parentLayout.setBackgroundColor(Config.context.getResources().getColor(R.color.darkPurple));
                    System.out.println(v.getBackground());
                    gradient(parentLayout);
                    oneIsCurrentlyChosen = true;
                }else{
                    if(v==img) {
                        vibIy(30);
                        removeButtons();
                        v.setSelected(false);
                        RelativeLayout parentLayout = (RelativeLayout) img.getParent();
                        parentLayout.setBackgroundColor(0x00000000);
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
