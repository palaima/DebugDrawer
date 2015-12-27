package io.palaima.debugdrawer.okhttp;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import io.palaima.debugdrawer.base.DebugModule;

public class OkHttpModule implements DebugModule {

    private static final boolean HAS_OKHTTP;

    static {
        boolean hasDependency;

        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
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

    public OkHttpModule(@NonNull OkHttpClient client) {
        if (!HAS_OKHTTP) {
            throw new RuntimeException("OkHttp dependency is not found");
        }
        this.client = client;
    }

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.dd_debug_drawer_module_okhttp, parent, false);

        okHttpCacheMaxSizeView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_max_size);
        okHttpCacheWriteErrorView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_write_error);
        okHttpCacheRequestCountView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_request_count);
        okHttpCacheNetworkCountView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_network_count);
        okHttpCacheHitCountView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_hit_count);

        Cache cache = client.getCache(); // Shares the cache with apiClient, so no need to check both.
        okHttpCacheMaxSizeView.setText(getSizeString(cache.getMaxSize()));

        refresh();
        return view;
    }

    private void refresh() {
        Cache cache = client.getCache(); // Shares the cache with apiClient, so no need to check both.
        int writeTotal = cache.getWriteSuccessCount() + cache.getWriteAbortCount();
        int percentage = (int) ((1f * cache.getWriteAbortCount() / writeTotal) * 100);
        okHttpCacheWriteErrorView.setText(
                cache.getWriteAbortCount() + " / " + writeTotal + " (" + percentage + "%)");
        okHttpCacheRequestCountView.setText(String.valueOf(cache.getRequestCount()));
        okHttpCacheNetworkCountView.setText(String.valueOf(cache.getNetworkCount()));
        okHttpCacheHitCountView.setText(String.valueOf(cache.getHitCount()));
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
