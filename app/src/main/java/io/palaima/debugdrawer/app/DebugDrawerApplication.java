package io.palaima.debugdrawer.app;

import com.squareup.leakcanary.LeakCanary;

import android.app.Application;


/**
 * @author Niklas Baudy (https://github.com/vanniktech)
 * @since 01/07/15
 */
public class DebugDrawerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
