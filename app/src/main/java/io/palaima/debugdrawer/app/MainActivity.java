package io.palaima.debugdrawer.app;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.palaima.debugdrawer.DebugDrawer;
import io.palaima.debugdrawer.actions.ActionsModule;
import io.palaima.debugdrawer.actions.models.ButtonAction;
import io.palaima.debugdrawer.actions.models.SpinnerAction;
import io.palaima.debugdrawer.actions.models.SwitchAction;
import io.palaima.debugdrawer.common.BuildModule;
import io.palaima.debugdrawer.fps.FpsModule;
import io.palaima.debugdrawer.location.LocationModule;
import io.palaima.debugdrawer.log.LogModule;
import io.palaima.debugdrawer.common.DeviceModule;
import io.palaima.debugdrawer.common.NetworkModule;
import io.palaima.debugdrawer.common.SettingsModule;
import io.palaima.debugdrawer.okhttp.OkHttpModule;
import io.palaima.debugdrawer.picasso.PicassoModule;
import io.palaima.debugdrawer.scalpel.ScalpelModule;
import jp.wasabeef.takt.Takt;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private DebugDrawer mDebugDrawer;

    private Picasso mPicasso;

    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        //change status bar color programmatically
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark_material_light));
        }*/


        if (BuildConfig.DEBUG) {
            SwitchAction switchAction = new SwitchAction("Test switch", new SwitchAction.Listener() {
                @Override
                public void onCheckedChanged(boolean value) {
                    Toast.makeText(MainActivity.this, "Switch checked", Toast.LENGTH_LONG).show();
                }
            });

            ButtonAction buttonAction = new ButtonAction("Test button", new ButtonAction.Listener() {
                @Override
                public void onClick() {
                    Toast.makeText(MainActivity.this, "Button clicked", Toast.LENGTH_LONG).show();
                }
            });

            SpinnerAction<String> spinnerAction = new SpinnerAction<>(
                Arrays.asList("First", "Second", "Third"),
                new SpinnerAction.OnItemSelectedListener<String>() {
                    @Override public void onItemSelected(String value) {
                        Toast.makeText(MainActivity.this, "Spinner item selected - " + value, Toast.LENGTH_LONG).show();
                    }
                }
            );

            mDebugDrawer = new DebugDrawer.Builder(this).modules(
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
            ).build();
        }

        showDummyLog();

        List<String> images = new ArrayList<>();
        for (int i = 1; i < 30; i++) {
            images.add("http://lorempixel.com/400/200/sports/" + i);
        }

        ListView listView = (ListView) findViewById(R.id.image_list);
        listView.setAdapter(new ImageAdapter(this, images, mPicasso));
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
    protected void onStart() {
        super.onStart();
        if (mDebugDrawer != null) {
            mDebugDrawer.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDebugDrawer != null) {
            mDebugDrawer.onStop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
