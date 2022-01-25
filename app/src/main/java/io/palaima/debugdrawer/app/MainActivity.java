package io.palaima.debugdrawer.app;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.palaima.debugdrawer.DebugDrawer;
import io.palaima.debugdrawer.actions.ActionsModule;
import io.palaima.debugdrawer.actions.ButtonAction;
import io.palaima.debugdrawer.actions.SpinnerAction;
import io.palaima.debugdrawer.actions.SwitchAction;
import io.palaima.debugdrawer.commons.BuildModule;
import io.palaima.debugdrawer.commons.DeviceModule;
import io.palaima.debugdrawer.commons.NetworkModule;
import io.palaima.debugdrawer.commons.SettingsModule;
import io.palaima.debugdrawer.fps.FpsModule;
import io.palaima.debugdrawer.glide.GlideModule;
import io.palaima.debugdrawer.location.LocationModule;
import io.palaima.debugdrawer.logs.LogsModule;
import io.palaima.debugdrawer.network.quality.NetworkQualityModule;
import io.palaima.debugdrawer.okhttp3.OkHttp3Module;
import io.palaima.debugdrawer.scalpel.ScalpelModule;
import io.palaima.debugdrawer.timber.TimberModule;
import jp.wasabeef.takt.Takt;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = createOkHttpClientBuilder(this.getApplication()).build();

        setupToolBar();

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
                @Override
                public void onItemSelected(String value) {
                    Toast.makeText(MainActivity.this, "Spinner item selected - " + value, Toast.LENGTH_LONG).show();
                }
            },
            1
        );

        new DebugDrawer.Builder(this).modules(
            new GlideModule(Glide.get(this)),
            new ActionsModule(switchAction, buttonAction, spinnerAction),
            new FpsModule(Takt.stock(getApplication())),
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
        ).withTheme(R.style.Theme_AppCompat).build();

        showDummyLog();

        List<String> images = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            images.add("https://unsplash.it/200/100?image=" + i);
        }

        ListView listView = findViewById(R.id.image_list);
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
        if (id == R.id.action_debug_view_activity) {
            startActivity(new Intent(this, DebugViewActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected Toolbar setupToolBar() {
        toolbar = findViewById(R.id.mainToolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        return toolbar;
    }

    private static final int DISK_CACHE_SIZE = 30 * 1024 * 1024; // 30 MB

    private static OkHttpClient.Builder createOkHttpClientBuilder(Application app) {
        // Install an HTTP cache in the application cache directory.
        File cacheDir = new File(app.getCacheDir(), "okhttp3-cache");
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
