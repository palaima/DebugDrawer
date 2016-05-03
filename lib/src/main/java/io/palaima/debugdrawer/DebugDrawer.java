/*
 * Copyright (C) 2015 Mantas Palaima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.palaima.debugdrawer;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import io.palaima.debugdrawer.modules.FpsModule;
import io.palaima.debugdrawer.util.UIUtils;
import io.palaima.debugdrawer.view.ScrimInsetsFrameLayout;
import okhttp3.OkHttpClient;

public class DebugDrawer {

    private final Builder builder;

    private final DrawerLayout drawerLayout;

    private ScrollView sliderLayout;

    private int drawerGravity;

    private DebugDrawer(Builder builder) {
        this.builder = builder;
        drawerLayout = builder.drawerLayout;
        drawerGravity = builder.drawerGravity;
        sliderLayout = builder.sliderLayout;
    }

    /**
     * Open the drawer
     */
    public void openDrawer() {
        if (drawerLayout != null && sliderLayout != null) {
            if (drawerGravity != 0) {
                drawerLayout.openDrawer(drawerGravity);
            } else {
                drawerLayout.openDrawer(sliderLayout);
            }
        }
    }

    /**
     * close the drawer
     */
    public void closeDrawer() {
        if (drawerLayout != null) {
            if (drawerGravity != 0) {
                drawerLayout.closeDrawer(drawerGravity);
            } else {
                drawerLayout.closeDrawer(sliderLayout);
            }
        }
    }

    /**
     * Get the current state of the drawer.
     * True if the drawer is currently open.
     */
    public boolean isDrawerOpen() {
        return drawerLayout != null && sliderLayout != null && drawerLayout.isDrawerOpen(sliderLayout);
    }

    /**
     * Enable or disable interaction with all drawers.
     */
    public void setDrawerLockMode(int lockMode) {
        if (drawerLayout != null && sliderLayout != null) {
            drawerLayout.setDrawerLockMode(lockMode);
        }
    }

    public static
    @CheckResult
    boolean checkActivity(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
        return rootView != null && rootView.getChildCount() != 0;
    }

    public void destroy() {
        for (DebugModule module : builder.debugModules) {
            if (module instanceof FpsModule) {
                ((FpsModule) module).close();
            }
        }
    }

    /**
     * Suggest to ues {@link DebugModuleListBuilder#addModule(DebugModule)#builder}
     */
    @Deprecated
    public static List<DebugModule> getDefaultModules(Activity activity) {
        return new DebugModuleListBuilder(activity.getApplicationContext())
                .addDefaultModules(activity).build();
    }

    /**
     * Suggest to ues {@link DebugModuleListBuilder#addDefaultModules(Activity, OkHttpClient)}
     */
    @Deprecated
    public static List<DebugModule> getDefaultModules(Activity activity, OkHttpClient okHttpClient) {
        return new DebugModuleListBuilder(activity.getApplicationContext())
                .addDefaultModules(activity, okHttpClient).build();
    }

    public static class Builder {

        private ViewGroup rootView;

        private Activity activity;

        private DrawerLayout drawerLayout;

        private ScrollView sliderLayout;

        private int drawerGravity = Gravity.END;

        //the width of the drawer
        private int drawerWidth = -1;

        private List<DebugModule> debugModules;

        private DrawerLayout.DrawerListener onDrawerListener;

        private ScrimInsetsFrameLayout drawerContentRoot;

        /**
         * Pass the activity you use the drawer in ;)
         * This is required if you want to set any values by resource
         */
        public Builder(Activity activity) {
            this.rootView = (ViewGroup) activity.findViewById(android.R.id.content);
            this.activity = activity;
        }

        /**
         * Pass the rootView of the Drawer which will be used to inflate the DrawerLayout in
         */
        public Builder rootView(ViewGroup rootView) {
            this.rootView = rootView;
            return this;
        }

        /**
         * Set the gravity for the drawer. START, LEFT | RIGHT, END
         */
        public Builder gravity(int gravity) {
            this.drawerGravity = gravity;
            return this;
        }

        /**
         * Set the Drawer width with a dp value
         */
        public Builder widthDp(int drawerWidthDp) {
            if (activity == null) {
                throw new RuntimeException("please pass an activity first to use this call");
            }
            this.drawerWidth = (int) TypedValue.applyDimension(1, drawerWidthDp,
                    activity.getResources().getDisplayMetrics());
            return this;
        }

        public Builder setDrawerListener(DrawerLayout.DrawerListener onDrawerListener) {
            this.onDrawerListener = onDrawerListener;
            return this;
        }

        /**
         * Add a initial DrawerItem or a DrawerItem Array for the Drawer
         */
        public Builder modules(List<DebugModule> drawerItems) {
            this.debugModules = drawerItems;
            return this;
        }

        /**
         * Build and add the Drawer to your activity
         */
        public DebugDrawer build() {
            if (activity == null) {
                throw new RuntimeException("please pass an activity");
            }

            if (rootView == null || rootView.getChildCount() == 0) {
                throw new RuntimeException(
                        "You have to set your layout for this activity with setContentView() first.");
            }

            drawerLayout = (DrawerLayout) activity.getLayoutInflater()
                    .inflate(R.layout.dd_debug_drawer, rootView, false);

            //get the content view
            View contentView = rootView.getChildAt(0);
            boolean alreadyInflated = contentView instanceof DrawerLayout;

            //get the drawer root
            drawerContentRoot = (ScrimInsetsFrameLayout) drawerLayout.getChildAt(0);

            //only add the new layout if it wasn't done before
            if (!alreadyInflated) {
                // remove the contentView
                rootView.removeView(contentView);
            } else {
                //if it was already inflated we have to clean up again
                rootView.removeAllViews();
            }

            //create the layoutParams to use for the contentView
            FrameLayout.LayoutParams layoutParamsContentView = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );

            //add the contentView to the drawer content frameLayout
            drawerContentRoot.addView(contentView, layoutParamsContentView);

            //add the drawerLayout to the root
            rootView.addView(drawerLayout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if (onDrawerListener != null) {
                        onDrawerListener.onDrawerSlide(drawerView, slideOffset);
                    }
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    if (onDrawerListener != null) {
                        onDrawerListener.onDrawerOpened(drawerView);
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    if (onDrawerListener != null) {
                        onDrawerListener.onDrawerClosed(drawerView);
                    }
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });

            sliderLayout = (ScrollView) drawerLayout.findViewById(R.id.dd_slider_layout);
            GridLayout moduleGl = ((GridLayout) sliderLayout.findViewById(R.id.dd_module_gl));

            // get the layout params
            DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) sliderLayout.getLayoutParams();
            if (params != null) {
                // if we've set a custom gravity set it
                if (drawerGravity != 0) {
                    params.gravity = drawerGravity;
                }
                // if this is a drawer from the right, change the margins :D
                params = processDrawerLayoutParams(params);
                // set the new layout params
                sliderLayout.setLayoutParams(params);
            }

            for (DebugModule module : debugModules) {
                inflateModules(module, moduleGl);
            }

            //create the result object
            DebugDrawer result = new DebugDrawer(this);

            //forget the reference to the activity
            activity = null;

            return result;
        }

        private void inflateModules(DebugModule module, GridLayout gl) {
            View titleV = LayoutInflater.from(activity).inflate(R.layout.dd_module_title, null, false);
            ((TextView) titleV.findViewById(R.id.title_tv)).setText(module.getName());
            GridLayout.LayoutParams params = getSpanColParams(gl);
            params.setMargins(0, 20, 0, 3);
            gl.addView(titleV, params);

            DebugWidgets widgets = module.createWidgets(new DebugWidgets.DebugWidgetsBuilder(activity));
            for (DebugWidgets.DebugWidgetsBuilder.DebugWidget widget : widgets.getWidgets()) {
                if (widget.title != null) {
                    // add title widget
                    TextView tv = new TextView(new ContextThemeWrapper(activity, R.style.Widget_DebugDrawer_Base_RowTitle));
                    tv.setText(widget.title);
                    gl.addView(tv);

                    // add summary widget
                    gl.addView(widget.view);
                } else {
                    gl.addView(widget.view, getSpanColParams(gl));
                }
            }
        }

        @NonNull
        private GridLayout.LayoutParams getSpanColParams(GridLayout gl) {
            // http://bbs.csdn.net/topics/391853628
            // 行设置，第一个为参数为第几行，默认可不设置，第二个参数为跨行数，没有则表示不跨行
            GridLayout.Spec row = GridLayout.spec(GridLayout.UNDEFINED);
            // 列设置，第一个为参数为第几列，默认可不设置，第二个参数为跨列数，没有则表示不跨行
            GridLayout.Spec col = GridLayout.spec(GridLayout.UNDEFINED, gl.getColumnCount());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
            params.width = GridLayout.LayoutParams.MATCH_PARENT;
            return params;
        }

        /**
         * helper to extend the layoutParams of the drawer
         */
        private DrawerLayout.LayoutParams processDrawerLayoutParams(DrawerLayout.LayoutParams params) {
            if (params != null) {
                if (drawerGravity != 0 && (drawerGravity == Gravity.RIGHT || drawerGravity == Gravity.END)) {
                    params.rightMargin = 0;
                    if (Build.VERSION.SDK_INT >= 17) {
                        params.setMarginEnd(0);
                    }

                    params.leftMargin = activity.getResources().getDimensionPixelSize(R.dimen.dd_debug_drawer_margin);
                    if (Build.VERSION.SDK_INT >= 17) {
                        params.setMarginEnd(activity.getResources().getDimensionPixelSize(R.dimen.dd_debug_drawer_margin));
                    }
                }

                if (drawerWidth > -1) {
                    params.width = drawerWidth;
                } else {
                    params.width = UIUtils.getOptimalDrawerWidth(activity);
                }
            }

            return params;
        }
    }
}
