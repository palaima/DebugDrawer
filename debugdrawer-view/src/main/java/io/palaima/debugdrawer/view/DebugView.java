package io.palaima.debugdrawer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import io.palaima.debugdrawer.base.DebugModule;

public class DebugView extends LinearLayout {

    private DebugModule[] drawerItems;

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
