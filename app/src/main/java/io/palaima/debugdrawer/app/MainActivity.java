package io.palaima.debugdrawer.app;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.palaima.debugdrawer.DebugDrawer;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    DebugDrawer debugDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OkHttpClient okHttpClient = createOkHttpClientBuilder(this.getApplication()).build();

        if (DebugDrawer.checkActivity(this)) {
            debugDrawer = new DebugDrawer.Builder(this)
                    .modules(DebugDrawer.getDefaultModules(this, okHttpClient))
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
