package io.palaima.debugdrawer.scalpel;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.jakewharton.scalpel.ScalpelFrameLayout;

import io.palaima.debugdrawer.base.DebugModule;

public class ScalpelModule implements DebugModule {


    private final Context mContext;
    private ViewGroup mRootView;

    public ScalpelModule(Activity activity) {
        mContext = activity;
        mRootView = (ViewGroup) activity.findViewById(android.R.id.content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {

        ViewGroup contentView = (ViewGroup) mRootView.getChildAt(0);
        ViewGroup scrimInsets = (ViewGroup) contentView.getChildAt(0);
        View contentRelativeView = scrimInsets.getChildAt(0);

        scrimInsets.removeView(contentRelativeView);

        final ScalpelFrameLayout scalpelFrameLayout = new ScalpelFrameLayout(mContext);
        scalpelFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT));
        scrimInsets.addView(scalpelFrameLayout);
        scalpelFrameLayout.addView(contentRelativeView);

        View view = inflater.inflate(R.layout.debug_drawer_item_scalpel, parent, false);
        Switch debugEnableScalpel = (Switch) view.findViewById(R.id.debug_enable_scalpel);
        debugEnableScalpel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelFrameLayout.setLayerInteractionEnabled(isChecked);
            }
        });
        Switch debugDisableGraphics = (Switch) view.findViewById(R.id.debug_disable_graphics);
        debugDisableGraphics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelFrameLayout.setDrawViews(!isChecked);
            }
        });
        Switch debugShowIds = (Switch) view.findViewById(R.id.debug_show_ids);
        debugShowIds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelFrameLayout.setDrawIds(isChecked);
            }
        });

        return view;
    }

    @Override
    public void onOpened() {

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
