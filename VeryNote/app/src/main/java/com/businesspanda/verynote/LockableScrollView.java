package com.businesspanda.verynote;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

/**
 * Created by Helene on 06.03.2015.
 */
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

    public boolean isScrollable() {
        return Scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (Scrollable) return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return Scrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!Scrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }


}
