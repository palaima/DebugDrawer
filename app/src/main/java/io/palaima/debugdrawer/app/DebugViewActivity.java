package io.palaima.debugdrawer.app;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
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
import io.palaima.debugdrawer.log.LogModule;
import io.palaima.debugdrawer.okhttp.OkHttpModule;
import io.palaima.debugdrawer.picasso.PicassoModule;
import io.palaima.debugdrawer.scalpel.ScalpelModule;
import io.palaima.debugdrawer.view.DebugView;
import jp.wasabeef.takt.Takt;
import timber.log.Timber;

public class DebugViewActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private DebugView mDebugView;

    private Picasso mPicasso;

    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debugview);
        mOkHttpClient = createOkHttpClient(this.getApplication());
        mPicasso = new Picasso.Builder(this)
            .downloader(new OkHttpDownloader(mOkHttpClient))
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
                @Override public void onItemSelected(String value) {
                    Toast.makeText(DebugViewActivity.this, "Spinner item selected - " + value, Toast.LENGTH_LONG).show();
                }
            }
        );

        mDebugView = (DebugView) findViewById(R.id.debug_view);

        mDebugView.modules(
            new ActionsModule(switchAction, buttonAction, spinnerAction),
            new FpsModule(Takt.stock(getApplication())),
            new LocationModule(this),
            new ScalpelModule(this),
            new LogModule(),
            new OkHttpModule(mOkHttpClient),
            new PicassoModule(mPicasso),
            new DeviceModule(this),
            new BuildModule(this),
            new NetworkModule(this),
            new SettingsModule(this)
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

    @Override protected void onResume() {
        super.onResume();
        mDebugView.onResume();
    }

    @Override protected void onPause() {
        super.onPause();
        mDebugView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDebugView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDebugView.onStop();
    }

    protected Toolbar setupToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        return mToolbar;
    }

    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50 MB

    private static OkHttpClient createOkHttpClient(Application application) {
        final OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);

        File cacheDir = new File(application.getCacheDir(), "http");
        client.setCache(new Cache(cacheDir, DISK_CACHE_SIZE));

        return client;
    }
}
