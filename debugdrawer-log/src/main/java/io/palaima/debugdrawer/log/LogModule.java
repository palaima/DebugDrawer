package io.palaima.debugdrawer.log;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.palaima.debugdrawer.base.DebugModule;
import io.palaima.debugdrawer.log.ui.LogDialog;

public class LogModule implements DebugModule {
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup parent) {
        View view = inflater.inflate(R.layout.debug_drawer_module_log, parent, false);

        view.findViewById(R.id.button_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogDialog(parent.getContext()).show();
            }
        });

        return view;
    }

    @Override
    public void onOpened() {

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
