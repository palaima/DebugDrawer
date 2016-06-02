package io.palaima.debugdrawer.modules;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;

import io.palaima.debugdrawer.DebugModule;
import io.palaima.debugdrawer.DebugWidgets;

/**
 * @author Kale
 * @date 2016/5/4
 */
public class ActivityModule implements DebugModule {

    private Context context;

    public ActivityModule(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public String getName() {
        return "Activity";
    }

    @Override
    public DebugWidgets createWidgets(DebugWidgets.DebugWidgetsBuilder builder) {
        String fullName = getRunningActivityName(context);
        String[] names = fullName.split("\\.");
        return builder.addText(null, names[names.length - 1]).build();
    }

    /**
     * @return 手机当前的activity
     */
    public static String getRunningActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
    }

}
