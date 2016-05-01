package io.palaima.debugdrawer.modules;

import android.support.annotation.NonNull;

import io.palaima.debugdrawer.DebugModule;
import io.palaima.debugdrawer.DebugWidgets;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class OkHttp3Module implements DebugModule {

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

    private final Cache cache;

    public OkHttp3Module(@NonNull OkHttpClient client) {
        if (!HAS_OKHTTP3) {
            throw new RuntimeException("OkHttp3 dependency is not found");
        }
        this.cache = client.cache();
    }

    @NonNull
    @Override
    public String getName() {
        return "OkHttp Cache";
    }

    @Override
    public DebugWidgets createWidgets(DebugWidgets.DebugWidgetsBuilder builder) {
        return builder.addText("Max Size", sizeString(cache.maxSize()))
                .addText("Write Errors", getWriteErrorCount())
                .addText("Request Count", cache.requestCount())
                .addText("Network Count", cache.networkCount())
                .addText("Hit Count", cache.hitCount())
                .build();
    }


    private String getWriteErrorCount() {
        int writeAbortCount = cache.writeAbortCount();
        int writeTotal = cache.writeSuccessCount() + writeAbortCount;
        int percentage = (int) ((1f * writeAbortCount / writeTotal) * 100);
        return String.valueOf(writeAbortCount) + " / " + writeTotal + " (" + percentage + "%)";
    }

    private static String sizeString(long bytes) {
        String[] units = new String[]{"B", "KB", "MB", "GB"};
        int unit = 0;
        while (bytes >= 1024) {
            bytes /= 1024;
            unit += 1;
        }
        return bytes + units[unit];
    }
}
