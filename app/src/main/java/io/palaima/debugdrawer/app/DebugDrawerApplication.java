package io.palaima.debugdrawer.app;

import com.squareup.leakcanary.LeakCanary;

import android.app.Application;

import io.palaima.debugdrawer.app.data.LumberYard;
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
