package io.palaima.debugdrawer.modules;

import android.support.annotation.NonNull;

import io.palaima.debugdrawer.DebugModule;
import io.palaima.debugdrawer.DebugWidgets;
import io.palaima.debugdrawer.util.LibUtil;
import okhttp3.Cache;
import okhttp3.OkHttpClient;

public class OkHttp3Module implements DebugModule {

    private final Cache cache;

    public OkHttp3Module(@NonNull OkHttpClient client) {
        if (!LibUtil.hasDependency("okhttp3.OkHttpClient")) {
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
