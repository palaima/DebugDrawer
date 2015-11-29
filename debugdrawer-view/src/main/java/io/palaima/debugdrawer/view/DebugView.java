package io.palaima.debugdrawer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import io.palaima.debugdrawer.base.DebugModule;

public class DebugView extends LinearLayout {

    private DebugModule[] mDrawerItems;

    public DebugView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public DebugView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public DebugView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    /**
     * Calls modules {@link DebugModule#onResume()} method
     */
    public void onResume() {
        if (mDrawerItems != null) {
            for (DebugModule drawerItem : mDrawerItems) {
                drawerItem.onResume();
            }
        }
    }

    /**
     * Calls modules {@link DebugModule#onPause()} method
     */
    public void onPause() {
        if (mDrawerItems != null) {
            for (DebugModule drawerItem : mDrawerItems) {
                drawerItem.onPause();
            }
        }
    }

    /**
     * Starts all modules and calls their {@link DebugModule#onStart()} method
     */
    public void onStart() {
        if (mDrawerItems != null) {
            for (DebugModule drawerItem : mDrawerItems) {
                drawerItem.onStart();
            }
        }
    }

    /**
     * Removes all modules and calls their {@link DebugModule#onStop()} method
     */
    public void onStop() {
        if (mDrawerItems != null) {
            for (DebugModule drawerItem : mDrawerItems) {
                drawerItem.onStop();
            }
        }
    }

    public void modules(DebugModule... drawerItems) {
        mDrawerItems = drawerItems;
        if (this.mDrawerItems != null && this.mDrawerItems.length != 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            DebugModule drawerItem;
            for (int i = 0; i < this.mDrawerItems.length; i++) {
                drawerItem = this.mDrawerItems[i];
                addView(drawerItem.onCreateView(inflater, this));
            }
        }
    }
}
