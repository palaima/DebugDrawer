package io.palaima.debugdrawer.okhttp3;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import io.palaima.debugdrawer.base.DebugModuleAdapter;
import okhttp3.OkHttpClient;

public class OkHttp3Module extends DebugModuleAdapter {

    private static final boolean HAS_OKHTTP3;

    static {
        boolean hasDependency;

        try {
            Class.forName("okhttp3.OkHttpClient");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_OKHTTP3 = hasDependency;
    }

    private final OkHttpClient client;

    private TextView okHttpCacheMaxSizeView;

    private TextView okHttpCacheWriteErrorView;

    private TextView okHttpCacheRequestCountView;

    private TextView okHttpCacheNetworkCountView;

    private TextView okHttpCacheHitCountView;

    public OkHttp3Module(@NonNull OkHttpClient client) {
        if (!HAS_OKHTTP3) {
            throw new RuntimeException("OkHttp3 dependency is not found");
        }
        this.client = client;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final View view = inflater.inflate(R.layout.dd_debug_drawer_module_okhttp3, parent, false);

        okHttpCacheMaxSizeView = view.findViewById(R.id.dd_debug_okhttp_cache_max_size);
        okHttpCacheWriteErrorView = view.findViewById(R.id.dd_debug_okhttp_cache_write_error);
        okHttpCacheRequestCountView = view.findViewById(R.id.dd_debug_okhttp_cache_request_count);
        okHttpCacheNetworkCountView = view.findViewById(R.id.dd_debug_okhttp_cache_network_count);
        okHttpCacheHitCountView = view.findViewById(R.id.dd_debug_okhttp_cache_hit_count);

        okHttpCacheMaxSizeView.setText(sizeString(maxSize()));

        view.findViewById(R.id.dd_debug_okhttp_cache_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCache();
            }
        });

        refresh();
        return view;
    }

    private void refresh() {
        final int writeTotal = writeSuccessCount() + writeAbortCount();
        final int percentage = (int) ((1f * writeAbortCount() / writeTotal) * 100);
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

    private void clearCache() {
        try {
            client.cache().evictAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long maxSize() {
        return client.cache().maxSize();
    }

    private int writeSuccessCount() {
        return client.cache().writeSuccessCount();
    }

    private int writeAbortCount() {
        return client.cache().writeAbortCount();
    }

    private int requestCount() {
        return client.cache().requestCount();
    }

    private int networkCount() {
        return client.cache().networkCount();
    }

    private int hitCount() {
        return client.cache().hitCount();
    }

    private static String sizeString(long bytes) {
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int unit = 0;
        while (bytes >= 1024) {
            bytes /= 1024;
            unit += 1;
        }
        return bytes + units[unit];
    }
}
