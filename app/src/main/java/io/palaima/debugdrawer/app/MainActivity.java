package io.palaima.debugdrawer.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.palaima.debugdrawer.DebugDrawer;
import io.palaima.debugdrawer.DebugModule;
import io.palaima.debugdrawer.modules.ActivityModule;
import io.palaima.debugdrawer.modules.BuildModule;
import io.palaima.debugdrawer.modules.DeviceModule;
import io.palaima.debugdrawer.modules.FpsModule;
import io.palaima.debugdrawer.modules.LogcatModule;
import io.palaima.debugdrawer.modules.NetworkModule;
import io.palaima.debugdrawer.modules.ScalpelModule;
import io.palaima.debugdrawer.modules.SettingsModule;
import jp.wasabeef.takt.Takt;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DebugDrawer debugDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient okHttpClient = createOkHttpClientBuilder(this.getApplication()).build();

        if (DebugDrawer.checkActivity(this)) {
            debugDrawer = new DebugDrawer.Builder(this)
                    .modules(getDebugModules(this))
                    .build();

            debugDrawer.openDrawer();
        }

        showDummyLog();

        List<String> images = new ArrayList<>();
        for (int i = 1; i < 30; i++) {
            images.add("http://lorempixel.com/400/200/sports/" + i);
        }

        ListView listView = (ListView) findViewById(R.id.image_list);
        assert listView != null;
        listView.setAdapter(new ImageAdapter(this, images));
    }

    public DebugDrawer.Builder getDrawerBuilder(Activity activity) {
        return new DebugDrawer.Builder(activity)
                .modules(getDebugModules(activity));
    }

    public List<DebugModule> getDebugModules(Activity activity) {
        List<DebugModule> list = Arrays.asList(
                new ActivityModule(getApplicationContext()),
                new NetworkModule(getApplicationContext()),
                new FpsModule(Takt.stock(getApplication())),
                new BuildModule(getApplicationContext()),
                new ScalpelModule(activity),
                new LogcatModule(activity),
                new DeviceModule(getApplicationContext()),
                new SettingsModule(activity));
        return new ArrayList<>(list);
    }

    private void showDummyLog() {
        Timber.d("Debug");
        Timber.e("Error");
        Timber.w("Warning");
        Timber.i("Info");
        Timber.v("Verbose");
        Timber.wtf("WTF");
    }


    private static final int DISK_CACHE_SIZE = 30 * 1024 * 1024; // 30 MB

    private static OkHttpClient.Builder createOkHttpClientBuilder(Application app) {
        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(app.getCacheDir(), "okhttp3-cache");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        return new OkHttpClient.Builder()
                .cache(cache)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        debugDrawer.destroy();
    }
}
