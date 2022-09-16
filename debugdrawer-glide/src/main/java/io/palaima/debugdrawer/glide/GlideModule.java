package io.palaima.debugdrawer.glide;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemoryCache;

import java.lang.reflect.Field;

import io.palaima.debugdrawer.base.DebugModuleAdapter;

public class GlideModule extends DebugModuleAdapter {

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

    @NonNull
    private final Glide glide;
    @NonNull
    private final MemoryCache memoryCache;

    private TextView poolSizeLabel;
    private TextView memCacheCurrentLabel;
    private TextView memCacheMaxLabel;
    private Button memCacheClearButton;
    private Button diskCacheClearButton;

    public GlideModule(@NonNull Glide glide) {
        if (!HAS_GLIDE) {
            throw new RuntimeException("Glide dependency is not found");
        }

        this.glide = glide;

        try {
            final Class<?> glideClass = glide.getClass();
            final Field field = glideClass.getDeclaredField("memoryCache");
            field.setAccessible(true);
            this.memoryCache = (MemoryCache) field.get(glide);
        } catch (Throwable t) {
            throw new RuntimeException("Incompatible Glide version", t);
        }
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final View view = inflater.inflate(R.layout.dd_debug_drawer_item_glide, parent, false);

        poolSizeLabel = view.findViewById(R.id.dd_debug_glide_pool_size);
        memCacheCurrentLabel = view.findViewById(R.id.dd_debug_glide_memcache_current);
        memCacheMaxLabel = view.findViewById(R.id.dd_debug_glide_memcache_max);
        memCacheClearButton = view.findViewById(R.id.dd_button_clear_memcache);
        diskCacheClearButton = view.findViewById(R.id.dd_button_clear_diskcache);

        memCacheClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                glide.clearMemory();
                refresh();
            }
        });

        diskCacheClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Needs to be called on a background thread
                        glide.clearDiskCache();
                    }
                }).start();
            }
        });
        refresh();
        return view;
    }

    @Override
    public void onOpened() {
        refresh();
    }

    @Override
    public void onResume() {
        refresh();
    }

    private void refresh() {
        final BitmapPool pool = glide.getBitmapPool();

        final String total = getSizeString(pool.getMaxSize());
        final String memCacheCurrent = getSizeString(memoryCache.getCurrentSize());
        final String memCacheMax = getSizeString(memoryCache.getMaxSize());

        poolSizeLabel.setText(total);
        memCacheCurrentLabel.setText(memCacheCurrent);
        memCacheMaxLabel.setText(memCacheMax);
    }

    private static String getSizeString(long bytes) {
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int unit = 0;
        while (bytes >= 1024) {
            bytes /= 1024;
            unit += 1;
        }
        return bytes + units[unit];
    }
}
