package io.palaima.debugdrawer.app;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import io.palaima.debugdrawer.log.data.LumberYard;
import timber.log.Timber;


/**
 * @author Niklas Baudy (https://github.com/vanniktech)
 * @since 01/07/15
 */
public class DebugDrawerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        LumberYard lumberYard = LumberYard.getInstance(this);
        lumberYard.cleanUp();

        Timber.plant(lumberYard.tree());
        Timber.plant(new Timber.DebugTree());
    }
}
