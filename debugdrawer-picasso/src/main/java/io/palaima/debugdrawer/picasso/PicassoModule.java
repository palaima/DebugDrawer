package io.palaima.debugdrawer.picasso;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import io.palaima.debugdrawer.module.DrawerModule;

public class PicassoModule implements DrawerModule {

    private final Picasso mPicasso;

    private Switch mIndicator;

    private TextView mCache;
    private TextView mCacheHits;
    private TextView mCacheMisses;

    private TextView mDecoded;
    private TextView mDecodedTotal;
    private TextView mDecodedAverage;

    private TextView mTransformed;
    private TextView mTransformedTotal;
    private TextView mTransformedAverage;

    public PicassoModule(Picasso picasso) {
        mPicasso = picasso;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {

        View view = inflater.inflate(R.layout.debug_drawer_item_picasso, parent, false);
        mIndicator = (Switch) view.findViewById(R.id.debug_picasso_indicators);
        mCache = (TextView) view.findViewById(R.id.debug_picasso_cache_size);
        mCacheHits = (TextView) view.findViewById(R.id.debug_picasso_cache_hit);
        mCacheMisses = (TextView) view.findViewById(R.id.debug_picasso_cache_miss);
        mDecoded = (TextView) view.findViewById(R.id.debug_picasso_decoded);
        mDecodedTotal = (TextView) view.findViewById(R.id.debug_picasso_decoded_total);
        mDecodedAverage = (TextView) view.findViewById(R.id.debug_picasso_decoded_avg);
        mTransformed = (TextView) view.findViewById(R.id.debug_picasso_transformed);
        mTransformedTotal = (TextView) view.findViewById(R.id.debug_picasso_transformed_total);
        mTransformedAverage = (TextView) view.findViewById(R.id.debug_picasso_transformed_avg);


        mPicasso.setIndicatorsEnabled(mPicasso.areIndicatorsEnabled());
        mIndicator.setChecked(mPicasso.areIndicatorsEnabled());
        mIndicator.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                        mPicasso.setIndicatorsEnabled(isChecked);
                    }
                });

        refresh();
        return view;
    }

    @Override
    public void onRefreshView() {
        refresh();
    }

    private void refresh() {
        StatsSnapshot snapshot = mPicasso.getSnapshot();
        String size = getSizeString(snapshot.size);
        String total = getSizeString(snapshot.maxSize);
        int percentage = (int) ((1f * snapshot.size / snapshot.maxSize) * 100);
        mCache.setText(size + " / " + total + " (" + percentage + "%)");
        mCacheHits.setText(String.valueOf(snapshot.cacheHits));
        mCacheMisses.setText(String.valueOf(snapshot.cacheMisses));
        mDecoded.setText(String.valueOf(snapshot.originalBitmapCount));
        mDecodedTotal.setText(getSizeString(snapshot.totalOriginalBitmapSize));
        mDecodedAverage.setText(getSizeString(snapshot.averageOriginalBitmapSize));
        mTransformed.setText(String.valueOf(snapshot.transformedBitmapCount));
        mTransformedTotal.setText(getSizeString(snapshot.totalTransformedBitmapSize));
        mTransformedAverage.setText(getSizeString(snapshot.averageTransformedBitmapSize));
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
