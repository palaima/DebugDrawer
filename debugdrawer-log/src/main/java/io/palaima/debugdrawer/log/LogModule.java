package io.palaima.debugdrawer.log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.palaima.debugdrawer.log.ui.LogDialog;
import io.palaima.debugdrawer.module.DrawerModule;

public class LogModule implements DrawerModule {
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
    public void onRefreshView() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
