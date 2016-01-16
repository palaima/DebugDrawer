package io.palaima.debugdrawer.okhttp;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

import io.palaima.debugdrawer.base.DebugModule;

public class OkHttpModule implements DebugModule {

    private static final boolean HAS_OKHTTP;
    private static final boolean HAS_OKHTTP3;

    static {
        boolean hasDependency;

        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_OKHTTP = hasDependency;

        try {
            Class.forName("okhttp3.OkHttpClient");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_OKHTTP3 = hasDependency;
    }

    private final OkHttpClient client;

    private final okhttp3.OkHttpClient client3;

    private TextView okHttpCacheMaxSizeView;

    private TextView okHttpCacheWriteErrorView;

    private TextView okHttpCacheRequestCountView;

    private TextView okHttpCacheNetworkCountView;

    private TextView okHttpCacheHitCountView;

    public OkHttpModule(@NonNull okhttp3.OkHttpClient client) {
        if (!HAS_OKHTTP3) {
            throw new RuntimeException("OkHttp3 dependency is not found");
        }
        this.client3 = client;
        this.client = null;
    }

    public OkHttpModule(@NonNull OkHttpClient client) {
        if (!HAS_OKHTTP) {
            throw new RuntimeException("OkHttp dependency is not found");
        }
        this.client = client;
        this.client3 = null;
    }

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.dd_debug_drawer_module_okhttp, parent, false);

        okHttpCacheMaxSizeView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_max_size);
        okHttpCacheWriteErrorView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_write_error);
        okHttpCacheRequestCountView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_request_count);
        okHttpCacheNetworkCountView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_network_count);
        okHttpCacheHitCountView = (TextView) view.findViewById(R.id.dd_debug_okhttp_cache_hit_count);

        okHttpCacheMaxSizeView.setText(sizeString(maxSize()));

        refresh();
        return view;
    }



    private void refresh() {
        int writeTotal = writeSuccessCount() + writeAbortCount();
        int percentage = (int) ((1f * writeAbortCount() / writeTotal) * 100);
        okHttpCacheWriteErrorView.setText(
            new StringBuilder().append(writeAbortCount())
                .append(" / ")
                .append(writeTotal)
                .append(" (")
                .append(percentage)
                .append("%)")
                .toString()
        );
        okHttpCacheRequestCountView.setText(String.valueOf(requestCount()));
        okHttpCacheNetworkCountView.setText(String.valueOf(networkCount()));
        okHttpCacheHitCountView.setText(String.valueOf(hitCount()));
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

    private static String sizeString(long bytes) {
        String[] units = new String[] { "B", "KB", "MB", "GB" };
        int unit = 0;
        while (bytes >= 1024) {
            bytes /= 1024;
            unit += 1;
        }
        return bytes + units[unit];
    }

    private long maxSize() {
        long value;
        if (client != null) {
            value = client.getCache().getMaxSize();
        } else {
            value = client3.cache().maxSize();
        }
        return value;
    }

    private int writeSuccessCount() {
        int value;
        if (client != null) {
            value = client.getCache().getWriteSuccessCount();
        } else {
            value = client3.cache().writeSuccessCount();
        }
        return value;
    }

    private int writeAbortCount() {
        int value;
        if (client != null) {
            value = client.getCache().getWriteAbortCount();
        } else {
            value = client3.cache().writeAbortCount();
        }
        return value;
    }

    private int requestCount() {
        int value;
        if (client != null) {
            value = client.getCache().getRequestCount();
        } else {
            value = client3.cache().requestCount();
        }
        return value;
    }

    private int networkCount() {
        int value;
        if (client != null) {
            value = client.getCache().getNetworkCount();
        } else {
            value = client3.cache().networkCount();
        }
        return value;
    }

    private int hitCount() {
        int value;
        if (client != null) {
            value = client.getCache().getHitCount();
        } else {
            value = client3.cache().hitCount();
        }
        return value;
    }
}
