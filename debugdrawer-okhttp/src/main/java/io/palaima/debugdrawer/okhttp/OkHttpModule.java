package io.palaima.debugdrawer.okhttp;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.palaima.debugdrawer.module.DrawerModule;

public class OkHttpModule implements DrawerModule {

    private final OkHttpClient mClient;

    private TextView mOkHttpCacheMaxSizeView;

    private TextView mOkHttpCacheWriteErrorView;

    private TextView mOkHttpCacheRequestCountView;

    private TextView mOkHttpCacheNetworkCountView;

    private TextView mOkHttpCacheHitCountView;

    public OkHttpModule(OkHttpClient client) {
        mClient = client;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.debug_drawer_module_okhttp, parent, false);

        mOkHttpCacheMaxSizeView = (TextView) view.findViewById(R.id.debug_okhttp_cache_max_size);
        mOkHttpCacheWriteErrorView = (TextView) view.findViewById(R.id.debug_okhttp_cache_write_error);
        mOkHttpCacheRequestCountView = (TextView) view.findViewById(R.id.debug_okhttp_cache_request_count);
        mOkHttpCacheNetworkCountView = (TextView) view.findViewById(R.id.debug_okhttp_cache_network_count);
        mOkHttpCacheHitCountView = (TextView) view.findViewById(R.id.debug_okhttp_cache_hit_count);

        Cache cache = mClient.getCache(); // Shares the cache with apiClient, so no need to check both.
        mOkHttpCacheMaxSizeView.setText(getSizeString(cache.getMaxSize()));

        refresh();
        return view;
    }

    private void refresh() {
        Cache cache = mClient.getCache(); // Shares the cache with apiClient, so no need to check both.
        int writeTotal = cache.getWriteSuccessCount() + cache.getWriteAbortCount();
        int percentage = (int) ((1f * cache.getWriteAbortCount() / writeTotal) * 100);
        mOkHttpCacheWriteErrorView.setText(
                cache.getWriteAbortCount() + " / " + writeTotal + " (" + percentage + "%)");
        mOkHttpCacheRequestCountView.setText(String.valueOf(cache.getRequestCount()));
        mOkHttpCacheNetworkCountView.setText(String.valueOf(cache.getNetworkCount()));
        mOkHttpCacheHitCountView.setText(String.valueOf(cache.getHitCount()));
    }

    @Override
    public void onRefreshView() {
        refresh();
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
