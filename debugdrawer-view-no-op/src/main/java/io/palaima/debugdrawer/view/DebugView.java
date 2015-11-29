package io.palaima.debugdrawer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import io.palaima.debugdrawer.base.DebugModule;

public class DebugView extends LinearLayout {

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

    }

    /**
     * Calls modules {@link DebugModule#onPause()} method
     */
    public void onPause() {

    }

    /**
     * Starts all modules and calls their {@link DebugModule#onStart()} method
     */
    public void onStart() {

    }

    /**
     * Removes all modules and calls their {@link DebugModule#onStop()} method
     */
    public void onStop() {

    }

    public void modules(DebugModule... drawerItems) {

    }
}
