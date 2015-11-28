package io.palaima.debugdrawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import io.palaima.debugdrawer.module.DrawerModule;

public class DebugView extends LinearLayout {

    private DrawerModule[] mDrawerItems;

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
     * Starts all modules and calls their {@link DrawerModule#onStart()} method
     */
    public void onStart() {
        for (DrawerModule drawerItem : mDrawerItems) {
            drawerItem.onStart();
        }
    }

    /**
     * Removes all modules and calls their {@link DrawerModule#onStop()} method
     */
    public void onStop() {
        for (DrawerModule drawerItem : mDrawerItems) {
            drawerItem.onStop();
        }
    }

    public void init(DrawerModule... drawerItems) {
        mDrawerItems = drawerItems;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (this.mDrawerItems != null && !(this.mDrawerItems.length == 0)) {
            DrawerModule drawerItem;
            for (int i = 0; i < this.mDrawerItems.length; i++) {
                drawerItem = this.mDrawerItems[i];
                addView(drawerItem.onCreateView(inflater, this));
            }
        }
    }
}
