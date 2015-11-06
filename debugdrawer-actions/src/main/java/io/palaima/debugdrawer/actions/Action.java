package io.palaima.debugdrawer.actions;

import android.view.View;
import android.widget.LinearLayout;

public interface Action {

    View getView(LinearLayout view);

    void onOpened();

    void onClosed();

    void onResume();

    void onPause();

    void onStart();

    void onStop();
}
