package io.palaima.debugdrawer.modules;

import android.support.annotation.NonNull;
import android.widget.CompoundButton;

import io.palaima.debugdrawer.DebugModule;
import io.palaima.debugdrawer.DebugWidgets;
import io.palaima.debugdrawer.util.LibUtil;
import jp.wasabeef.takt.Audience;
import jp.wasabeef.takt.Takt;


public class FpsModule implements DebugModule {

    public boolean isShown = false;

    private final Takt.Program program;

    public FpsModule(@NonNull final Takt.Program program) {
        if (!LibUtil.hasDependency("jp.wasabeef.takt.Takt")) {
            throw new RuntimeException("Takt dependency is not found");
        }

        this.program = program.listener(new Audience() {
            @Override
            public void heartbeat(double fps) {
                if (fps >= 56) {
                    program.color(0xff00ff00); // green
                } else if (fps < 40) {
                    program.color(0xffff0000); // red 
                } else {
                    program.color(0xffffc107); // yellow
                }
            }
        });
    }

    @NonNull
    @Override
    public String getName() {
        return "Fps";
    }

    @Override
    public DebugWidgets createWidgets(DebugWidgets.DebugWidgetsBuilder builder) {
        return builder.addSwitch("Show fps", false, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isShown = true;
                    program.play();
                } else {
                    isShown = false;
                    program.stop();
                }
            }
        }).build();
    }

    public void close() {
        if (isShown) {
            program.stop();
        }
    }

}
