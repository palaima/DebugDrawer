package io.palaima.debugdrawer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import io.palaima.debugdrawer.base.DebugModule;

public class DebugView extends LinearLayout {

    private DebugModule[] drawerItems;

    public DebugView(Context context) {
        super(context);
        init(context, null, null, null);
    }

    public DebugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, null, null);
    }

    public DebugView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, null);
    }

    public DebugView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init(Context context, AttributeSet attributeSet, Integer defStyleAttr, Integer defStyleRes) {
        setOrientation(VERTICAL);

        TypedArray a;
        if (defStyleAttr == null || defStyleRes == null) {
            a = context.obtainStyledAttributes(attributeSet, new int[]{android.R.attr.windowBackground});
        } else {
            a = context.obtainStyledAttributes(attributeSet, new int[]{android.R.attr.windowBackground}, defStyleAttr, defStyleRes);
        }

        Drawable windowBackground = a.getDrawable(0);
        a.recycle();
        setBackgroundDrawable(windowBackground);
    }

    /**
     * Calls modules {@link DebugModule#onResume()} method
     */
    public void onResume() {
        if (drawerItems != null) {
            for (DebugModule drawerItem : drawerItems) {
                drawerItem.onResume();
            }
        }
    }

    /**
     * Calls modules {@link DebugModule#onPause()} method
     */
    public void onPause() {
        if (drawerItems != null) {
            for (DebugModule drawerItem : drawerItems) {
                drawerItem.onPause();
            }
        }
    }

    /**
     * Starts all modules and calls their {@link DebugModule#onStart()} method
     */
    public void onStart() {
        if (drawerItems != null) {
            for (DebugModule drawerItem : drawerItems) {
                drawerItem.onStart();
            }
        }
    }

    /**
     * Removes all modules and calls their {@link DebugModule#onStop()} method
     */
    public void onStop() {
        if (drawerItems != null) {
            for (DebugModule drawerItem : drawerItems) {
                drawerItem.onStop();
            }
        }
    }

    public void modules(DebugModule... drawerItems) {
        this.drawerItems = drawerItems;
        if (this.drawerItems != null && this.drawerItems.length != 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            DebugModule drawerItem;
            for (int i = 0; i < this.drawerItems.length; i++) {
                drawerItem = this.drawerItems[i];
                addView(drawerItem.onCreateView(inflater, this));
            }
        }
    }
}
