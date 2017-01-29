package io.palaima.debugdrawer.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import io.palaima.debugdrawer.DebugDrawer;

public class DebugDrawerLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private Activity mActivity;
    private DebugDrawer mDebugDrawer;

    public DebugDrawerLifecycleCallbacks(Activity activity, DebugDrawer debugDrawer) {
        mActivity = activity;
        mDebugDrawer = debugDrawer;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (mActivity == activity) {
            mDebugDrawer.onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (mActivity == activity) {
            mDebugDrawer.onResume();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (mActivity == activity) {
            mDebugDrawer.onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (mActivity == activity) {
            mDebugDrawer.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
        mActivity = null;
        mDebugDrawer = null;
    }
}
