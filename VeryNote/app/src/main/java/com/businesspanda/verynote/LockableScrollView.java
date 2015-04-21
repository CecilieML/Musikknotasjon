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
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

// Custom scrollView where scrolling can be disabled
public class LockableScrollView extends HorizontalScrollView {

    private boolean Scrollable = true;

    public LockableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LockableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableScrollView(Context context) {
        super(context);
    }

    public void setScrollingEnabled(boolean enabled) {
        Scrollable = enabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (Scrollable) return super.onTouchEvent(ev);
                return Scrollable;
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!Scrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }


}
