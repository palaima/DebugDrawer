package io.palaima.debugdrawer.fps;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import io.palaima.debugdrawer.module.DrawerModule;
import jp.wasabeef.takt.Takt;

public class FpsModule implements DrawerModule {

    private final Takt.Program program;

    private boolean isChecked;

    public FpsModule(@NonNull Takt.Program program) {
        this.program = program;
    }

    @Override @NonNull public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.debug_drawer_item_fps, parent, false);
        Switch showSwitch = (Switch) view.findViewById(R.id.debug_fps);
        showSwitch.setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                    FpsModule.this.isChecked = isChecked;
                    if (isChecked) {
                        program.play();
                    } else {
                        program.stop();
                    }
                }
            });
        return view;
    }

    @Override public void onOpened() {

    }

    @Override public void onClosed() {

    }

    @Override public void onStart() {
        if (isChecked) {
            program.play();
        }
    }

    @Override public void onStop() {
        if (isChecked) {
            program.stop();
        }
    }
}
