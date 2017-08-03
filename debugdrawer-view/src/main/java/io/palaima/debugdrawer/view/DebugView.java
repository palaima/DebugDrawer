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
        this(context, null);
    }

    public DebugView(Context context, AttributeSet attrSet) {
        this(context, attrSet, 0);
    }

    public DebugView(Context context, AttributeSet attrSet, int defStyleAttr) {
        super(context, attrSet, defStyleAttr);
        setOrientation(VERTICAL);

        int[] attrs = { android.R.attr.windowBackground };
        TypedArray a = context.obtainStyledAttributes(attrSet, attrs);
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
