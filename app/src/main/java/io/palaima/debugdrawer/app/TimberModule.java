package io.palaima.debugdrawer.app;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.palaima.debugdrawer.app.ui.LogDialog;

public class TimberModule  {

    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull final ViewGroup parent) {
/*
//        View view = inflater.inflate(io.palaima.debugdrawer.timber.R.layout.dd_debug_drawer_module_log, parent, false);

        view.findViewById(io.palaima.debugdrawer.timber.R.id.dd_button_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        return view;
*/
        new LogDialog(parent.getContext()).show();
        return null;
    }
}
