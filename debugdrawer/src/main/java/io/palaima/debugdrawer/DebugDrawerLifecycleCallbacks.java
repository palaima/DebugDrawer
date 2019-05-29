package io.palaima.debugdrawer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

final class DebugDrawerLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private Activity activity;
    private DebugDrawer debugDrawer;

    DebugDrawerLifecycleCallbacks(Activity activity, DebugDrawer debugDrawer) {
        this.activity = activity;
        this.debugDrawer = debugDrawer;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (this.activity == activity) {
            debugDrawer.onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (this.activity == activity) {
            debugDrawer.onResume();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (this.activity == activity) {
            debugDrawer.onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (this.activity == activity) {
            debugDrawer.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (this.activity == activity) {
            this.activity.getApplication().unregisterActivityLifecycleCallbacks(this);
            this.activity = null;
            debugDrawer = null;
        }
    }
}
