package io.palaima.debugdrawer.picasso;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;

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

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        final View view = inflater.inflate(R.layout.dd_debug_drawer_item_picasso, parent, false);
        indicatorView = view.findViewById(R.id.dd_debug_picasso_indicators);
        cacheLabel = view.findViewById(R.id.dd_debug_picasso_cache_size);
        cacheHitsLabel = view.findViewById(R.id.dd_debug_picasso_cache_hit);
        cacheMissesLabel = view.findViewById(R.id.dd_debug_picasso_cache_miss);
        decodedLabel = view.findViewById(R.id.dd_debug_picasso_decoded);
        decodedTotalLabel = view.findViewById(R.id.dd_debug_picasso_decoded_total);
        decodedAverageLabel = view.findViewById(R.id.dd_debug_picasso_decoded_avg);
        transformedLabel = view.findViewById(R.id.dd_debug_picasso_transformed);
        transformedTotalLabel = view.findViewById(R.id.dd_debug_picasso_transformed_total);
        transformedAverageLabel = view.findViewById(R.id.dd_debug_picasso_transformed_avg);


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
        final StatsSnapshot snapshot = picasso.getSnapshot();
        final String size = getSizeString(snapshot.size);
        final String total = getSizeString(snapshot.maxSize);
        final int percentage = (int) ((1f * snapshot.size / snapshot.maxSize) * 100);
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
        final String[] units = new String[]{"B", "KB", "MB", "GB"};
        int unit = 0;
        while (bytes >= 1024) {
            bytes /= 1024;
            unit += 1;
        }
        return bytes + units[unit];
    }
}
