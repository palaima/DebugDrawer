package io.palaima.debugdrawer.actions;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public interface Action {

    View getView(LayoutInflater inflater, LinearLayout parent);

    void onOpened();

    void onClosed();

    void onResume();

    void onPause();

    void onStart();

    void onStop();
}
