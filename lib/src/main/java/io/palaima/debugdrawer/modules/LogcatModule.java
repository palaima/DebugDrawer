package io.palaima.debugdrawer.modules;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import io.palaima.debugdrawer.DebugWidgets;
import io.palaima.debugdrawer.DebugModule;
import io.palaima.debugdrawer.util.LibUtil;
import kale.debug.log.ui.LogActivity;

/**
 * @author Kale
 * @date 2016/3/26
 */
public class LogcatModule implements DebugModule {

    private Activity activity;

    public LogcatModule(Activity activity) {
        if (!LibUtil.hasDependency("kale.debug.log.ui.LogActivity")) {
            throw new RuntimeException("Logcat dependency is not found");
        }
        
        this.activity = activity;
    }

    @NonNull
    @Override
    public String getName() {
        return "Logcat";
    }

    @Override
    public DebugWidgets createWidgets(DebugWidgets.DebugWidgetsBuilder builder) {
        return builder.addButton("Show App Log", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, LogActivity.class));
            }
        }).build();
    }
}
