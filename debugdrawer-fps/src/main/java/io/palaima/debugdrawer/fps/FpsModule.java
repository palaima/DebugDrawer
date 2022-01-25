package io.palaima.debugdrawer.fps;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import io.palaima.debugdrawer.base.DebugModuleAdapter;
import jp.wasabeef.takt.Takt;

public class FpsModule extends DebugModuleAdapter {

    private static final boolean HAS_TAKT;

    static {
        boolean hasDependency;

        try {
            Class.forName("jp.wasabeef.takt.Takt");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_TAKT = hasDependency;
    }

    private final Takt.Program program;

    private boolean isChecked;

    public FpsModule(@NonNull Takt.Program program) {
        if (!HAS_TAKT) {
            throw new RuntimeException("Takt dependency is not found");
        }

        this.program = program;
    }

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final View view = inflater.inflate(R.layout.dd_debug_drawer_item_fps, parent, false);
        final Switch showSwitch = (Switch) view.findViewById(R.id.dd_debug_fps);
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

    @Override
    public void onResume() {
        if (isChecked) {
            program.play();
        }
    }

    @Override
    public void onPause() {
        if (isChecked) {
            program.stop();
        }
    }
}
