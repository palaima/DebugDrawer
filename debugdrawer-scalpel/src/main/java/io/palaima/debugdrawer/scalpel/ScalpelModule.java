package io.palaima.debugdrawer.scalpel;


import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.jakewharton.scalpel.ScalpelFrameLayout;

import io.palaima.debugdrawer.base.DebugModuleAdapter;

public class ScalpelModule extends DebugModuleAdapter {

    private static final boolean HAS_SCALPEL;

    static {
        boolean hasDependency;

        try {
            Class.forName("com.jakewharton.scalpel.ScalpelFrameLayout");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_SCALPEL = hasDependency;
    }

    private final Context context;
    private ViewGroup rootView;

    public ScalpelModule(@NonNull Activity activity) {
        if (!HAS_SCALPEL) {
            throw new RuntimeException("Scalpel dependency is not found");
        }
        context = activity;
        rootView = (ViewGroup) activity.findViewById(android.R.id.content);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        final ViewGroup contentView = (ViewGroup) rootView.getChildAt(0);
        final ViewGroup scrimInsets = (ViewGroup) contentView.getChildAt(0);
        final View contentRelativeView = scrimInsets.getChildAt(0);

        scrimInsets.removeView(contentRelativeView);

        final ScalpelFrameLayout scalpelFrameLayout = new ScalpelFrameLayout(context);
        scalpelFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT));
        scrimInsets.addView(scalpelFrameLayout);
        scalpelFrameLayout.addView(contentRelativeView);

        final View view = inflater.inflate(R.layout.dd_debug_drawer_item_scalpel, parent, false);
        final Switch debugEnableScalpel = view.findViewById(R.id.dd_debug_enable_scalpel);
        debugEnableScalpel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelFrameLayout.setLayerInteractionEnabled(isChecked);
            }
        });
        final Switch debugDisableGraphics = view.findViewById(R.id.dd_debug_disable_graphics);
        debugDisableGraphics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelFrameLayout.setDrawViews(!isChecked);
            }
        });
        final Switch debugShowIds = view.findViewById(R.id.dd_debug_show_ids);
        debugShowIds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scalpelFrameLayout.setDrawIds(isChecked);
            }
        });

        return view;
    }
}
