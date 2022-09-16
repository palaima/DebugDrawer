package io.palaima.debugdrawer.app;

import android.app.Application;


import io.palaima.debugdrawer.timber.data.LumberYard;
import timber.log.Timber;

public class DebugDrawerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LumberYard lumberYard = LumberYard.getInstance(this);
        lumberYard.cleanUp();

        Timber.plant(lumberYard.tree());
        Timber.plant(new Timber.DebugTree());
    }
}
