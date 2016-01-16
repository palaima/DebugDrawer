package io.palaima.debugdrawer.okhttp3;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

import io.palaima.debugdrawer.base.DebugModule;

public class OkHttp3Module implements DebugModule {

    private static final boolean HAS_OKHTTP;

    static {
        boolean hasDependency;

        try {
            Class.forName("okhttp3.OkHttpClient");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_OKHTTP = hasDependency;
    }

    private final OkHttpClient client;

    private TextView okHttpCacheMaxSizeView;

    private TextView okHttpCacheWriteErrorView;

    private TextView okHttpCacheRequestCountView;

    private TextView okHttpCacheNetworkCountView;

    private TextView okHttpCacheHitCountView;

    public OkHttp3Module(@NonNull OkHttpClient client) {
        if (!HAS_OKHTTP) {
            throw new RuntimeException("OkHttp dependency is not found");
        }
        this.client = client;
    }

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.dd_debug_drawer_module_okhttp3, parent, false);

        okHttpCacheMaxSizeView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_max_size);
        okHttpCacheWriteErrorView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_write_error);
        okHttpCacheRequestCountView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_request_count);
        okHttpCacheNetworkCountView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_network_count);
        okHttpCacheHitCountView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_hit_count);

        Cache cache = client.cache(); // Shares the cache with apiClient, so no need to check both.
        okHttpCacheMaxSizeView.setText(getSizeString(cache.maxSize()));

        refresh();
        return view;
    }

    private void refresh() {
        Cache cache = client.cache(); // Shares the cache with apiClient, so no need to check both.
        int writeSuccess = cache.writeSuccessCount();
        int writeAbort = cache.writeAbortCount();
        int writeTotal = writeSuccess + writeAbort;
        int percentage = (int) ((1f * writeAbort / writeTotal) * 100);
        okHttpCacheWriteErrorView.setText(
                writeAbort + " / " + writeTotal + " (" + percentage + "%)");
        okHttpCacheRequestCountView.setText(String.valueOf(cache.requestCount()));
        okHttpCacheNetworkCountView.setText(String.valueOf(cache.networkCount()));
        okHttpCacheHitCountView.setText(String.valueOf(cache.hitCount()));
    }

    @Override
    public void onOpened() {
        refresh();
    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    private static String getSizeString(long bytes) {
        String[] units = new String[] { "B", "KB", "MB", "GB" };
        int unit = 0;
        while (bytes >= 1024) {
            bytes /= 1024;
            unit += 1;
        }
        return bytes + units[unit];
    }
}
