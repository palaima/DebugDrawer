package io.palaima.debugdrawer.actions;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public interface Action {

    View getView(@NonNull final LayoutInflater inflater, @NonNull final LinearLayout parent);

    void onOpened();

    void onClosed();

    void onResume();

    void onPause();

    void onStart();

    void onStop();
}
