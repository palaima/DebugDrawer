package io.palaima.debugdrawer.app;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.palaima.debugdrawer.actions.ActionsModule;
import io.palaima.debugdrawer.actions.ButtonAction;
import io.palaima.debugdrawer.actions.SpinnerAction;
import io.palaima.debugdrawer.actions.SwitchAction;
import io.palaima.debugdrawer.commons.BuildModule;
import io.palaima.debugdrawer.commons.DeviceModule;
import io.palaima.debugdrawer.commons.NetworkModule;
import io.palaima.debugdrawer.commons.SettingsModule;
import io.palaima.debugdrawer.fps.FpsModule;
import io.palaima.debugdrawer.location.LocationModule;
import io.palaima.debugdrawer.logs.LogsModule;
import io.palaima.debugdrawer.network.quality.NetworkQualityModule;
import io.palaima.debugdrawer.okhttp3.OkHttp3Module;
import io.palaima.debugdrawer.picasso.PicassoModule;
import io.palaima.debugdrawer.scalpel.ScalpelModule;
import io.palaima.debugdrawer.timber.TimberModule;
import io.palaima.debugdrawer.view.DebugView;
import jp.wasabeef.takt.Takt;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class DebugViewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DebugView debugView;

    private OkHttpClient okHttpClient;
    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugview);
        okHttpClient = createOkHttpClientBuilder(this.getApplication()).build();
        picasso = new Picasso.Builder(this)
            .downloader(new OkHttp3Downloader(okHttpClient))
            .listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
                    Log.e("Picasso", "Failed to load image: %s", e);
                }
            })
            .build();

        setupToolBar();

        SwitchAction switchAction = new SwitchAction("Test switch", new SwitchAction.Listener() {
            @Override
            public void onCheckedChanged(boolean value) {
                Toast.makeText(DebugViewActivity.this, "Switch checked", Toast.LENGTH_LONG).show();
            }
        });

        ButtonAction buttonAction = new ButtonAction("Test button", new ButtonAction.Listener() {
            @Override
            public void onClick() {
                Toast.makeText(DebugViewActivity.this, "Button clicked", Toast.LENGTH_LONG).show();
            }
        });

        SpinnerAction<String> spinnerAction = new SpinnerAction<>(
            Arrays.asList("First", "Second", "Third"),
            new SpinnerAction.OnItemSelectedListener<String>() {
                @Override
                public void onItemSelected(String value) {
                    Toast.makeText(DebugViewActivity.this, "Spinner item selected - " + value, Toast.LENGTH_LONG).show();
                }
            }
        );

        debugView = findViewById(R.id.debug_view);

        debugView.modules(
            new ActionsModule(switchAction, buttonAction, spinnerAction),
            new FpsModule(Takt.stock(getApplication())),
            new PicassoModule(picasso),
            new LocationModule(),
            new LogsModule(),
            new ScalpelModule(this),
            new TimberModule(),
            new OkHttp3Module(okHttpClient),
            new NetworkQualityModule(this),
            new DeviceModule(),
            new BuildModule(),
            new NetworkModule(),
            new SettingsModule()
        );

        showDummyLog();
    }

    private void showDummyLog() {
        Timber.d("Debug");
        Timber.e("Error");
        Timber.w("Warning");
        Timber.i("Info");
        Timber.v("Verbose");
        Timber.wtf("WTF");
    }

    @Override
    protected void onResume() {
        super.onResume();
        debugView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        debugView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        debugView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        debugView.onStop();
    }

    private Toolbar setupToolBar() {
        toolbar = findViewById(R.id.mainToolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }

    private static final int DISK_CACHE_SIZE = 20 * 1024 * 1024; // 20 MB

    private static OkHttpClient.Builder createOkHttpClientBuilder(Application app) {
        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(app.getCacheDir(), "okhttp3");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        return new OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(LogsModule.chuckInterceptor(app))
            .addInterceptor(NetworkQualityModule.interceptor(app))
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS);
    }
}
