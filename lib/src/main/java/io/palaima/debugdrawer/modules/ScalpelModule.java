package io.palaima.debugdrawer.modules;


import com.jakewharton.scalpel.ScalpelFrameLayout;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import io.palaima.debugdrawer.DebugModule;
import io.palaima.debugdrawer.DebugWidgets;
import io.palaima.debugdrawer.util.LibUtil;


public class ScalpelModule implements DebugModule {

    private final ScalpelFrameLayout scalpelFrameLayout;

    public ScalpelModule(@NonNull Activity activity) {
        if (!LibUtil.hasDependency("com.jakewharton.scalpel.ScalpelFrameLayout")) {
            throw new RuntimeException("Scalpel dependency is not found");
        }

        scalpelFrameLayout = new ScalpelFrameLayout(activity);
        scalpelFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
        ViewGroup contentView = (ViewGroup) rootView.getChildAt(0);
        rootView.removeView(contentView);

        rootView.addView(scalpelFrameLayout);
        scalpelFrameLayout.addView(contentView);

        scalpelFrameLayout.setDrawViews(true);
        scalpelFrameLayout.setDrawIds(true);

    }

    @NonNull
    @Override
    public String getName() {
        return "Scalpel";
    }

    @Override
    public DebugWidgets createWidgets(DebugWidgets.DebugWidgetsBuilder builder) {
        return builder.addSwitch("Scalpel", false, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelFrameLayout.setLayerInteractionEnabled(isChecked);
            }
        }).addSwitch("Graphics", true, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelFrameLayout.setDrawViews(isChecked);
            }
        }).addSwitch("Show Ids", true, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelFrameLayout.setDrawIds(isChecked);
            }
        }).build();
    }
}
