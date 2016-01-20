package io.palaima.debugdrawer.glide;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;

import java.lang.reflect.Field;

import io.palaima.debugdrawer.base.DebugModule;

public class GlideModule implements DebugModule {

    private static final boolean HAS_GLIDE;

    static {
        boolean hasDependency;

        try {
            Class.forName("com.bumptech.glide.Glide");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_GLIDE = hasDependency;
    }

    @NonNull private final Glide       glide;
    @NonNull private final MemoryCache memoryCache;

    public GlideModule(@NonNull Glide glide) {
        if (!HAS_GLIDE) {
            throw new RuntimeException("Glide dependency is not found");
        }

        this.glide = glide;

        try {
            Class<?> glideClass = glide.getClass();
            Field field = glideClass.getDeclaredField("memoryCache");
            field.setAccessible(true);
            this.memoryCache = (MemoryCache) field.get(glide);
        } catch (Throwable t) {
            throw new RuntimeException("Incompatible Glide version", t);
        }
    }

    @NonNull @Override public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.dd_debug_drawer_item_glide, parent, false);

        refresh();
        return view;
    }

    @Override public void onOpened() {
        refresh();
    }

    @Override public void onClosed() {

    }

    @Override public void onResume() {
        refresh();
    }

    @Override public void onPause() {

    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    private void refresh() {
        BitmapPool pool = glide.getBitmapPool();
        String total = getSizeString(pool.getMaxSize());
        String memCacheCurrent = getSizeString(memoryCache.getCurrentSize());
        String memCacheMax = getSizeString(memoryCache.getMaxSize());

        poolSizeLabel.setText(total);
        memCacheCurrentLabel.setText(memCacheCurrent);
        memCacheMaxLabel.setText(memCacheMax);
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
