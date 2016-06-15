package io.palaima.debugdrawer.picasso;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;

import io.palaima.debugdrawer.base.DebugModule;
import io.palaima.debugdrawer.base.DebugModuleAdapter;

public class PicassoModule extends DebugModuleAdapter {

    private static final boolean HAS_PICASSO;

    static {
        boolean hasDependency;

        try {
            Class.forName("com.squareup.picasso.Picasso");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_PICASSO = hasDependency;
    }

    private final Picasso picasso;

    private Switch indicatorView;

    private TextView cacheLabel;
    private TextView cacheHitsLabel;
    private TextView cacheMissesLabel;

    private TextView decodedLabel;
    private TextView decodedTotalLabel;
    private TextView decodedAverageLabel;

    private TextView transformedLabel;
    private TextView transformedTotalLabel;
    private TextView transformedAverageLabel;

    public PicassoModule(@NonNull Picasso picasso) {
        if (!HAS_PICASSO) {
            throw new RuntimeException("Picasso dependency is not found");
        }
        this.picasso = picasso;
    }

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        View view = inflater.inflate(R.layout.dd_debug_drawer_item_picasso, parent, false);
        indicatorView = (Switch) view.findViewById(R.id.dd_debug_picasso_indicators);
        cacheLabel = (TextView) view.findViewById(R.id.dd_debug_picasso_cache_size);
        cacheHitsLabel = (TextView) view.findViewById(R.id.dd_debug_picasso_cache_hit);
        cacheMissesLabel = (TextView) view.findViewById(R.id.dd_debug_picasso_cache_miss);
        decodedLabel = (TextView) view.findViewById(R.id.dd_debug_picasso_decoded);
        decodedTotalLabel = (TextView) view.findViewById(R.id.dd_debug_picasso_decoded_total);
        decodedAverageLabel = (TextView) view.findViewById(R.id.dd_debug_picasso_decoded_avg);
        transformedLabel = (TextView) view.findViewById(R.id.dd_debug_picasso_transformed);
        transformedTotalLabel = (TextView) view.findViewById(R.id.dd_debug_picasso_transformed_total);
        transformedAverageLabel = (TextView) view.findViewById(R.id.dd_debug_picasso_transformed_avg);


        picasso.setIndicatorsEnabled(picasso.areIndicatorsEnabled());
        indicatorView.setChecked(picasso.areIndicatorsEnabled());
        indicatorView.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                        picasso.setIndicatorsEnabled(isChecked);
                    }
                });

        refresh();
        return view;
    }

    @Override
    public void onOpened() {
        refresh();
    }

    private void refresh() {
        StatsSnapshot snapshot = picasso.getSnapshot();
        String size = getSizeString(snapshot.size);
        String total = getSizeString(snapshot.maxSize);
        int percentage = (int) ((1f * snapshot.size / snapshot.maxSize) * 100);
        cacheLabel.setText(size + " / " + total + " (" + percentage + "%)");
        cacheHitsLabel.setText(String.valueOf(snapshot.cacheHits));
        cacheMissesLabel.setText(String.valueOf(snapshot.cacheMisses));
        decodedLabel.setText(String.valueOf(snapshot.originalBitmapCount));
        decodedTotalLabel.setText(getSizeString(snapshot.totalOriginalBitmapSize));
        decodedAverageLabel.setText(getSizeString(snapshot.averageOriginalBitmapSize));
        transformedLabel.setText(String.valueOf(snapshot.transformedBitmapCount));
        transformedTotalLabel.setText(getSizeString(snapshot.totalTransformedBitmapSize));
        transformedAverageLabel.setText(getSizeString(snapshot.averageTransformedBitmapSize));
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
