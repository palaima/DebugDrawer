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
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.drawerlayout.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import io.palaima.debugdrawer.base.DebugModule;
import io.palaima.debugdrawer.util.UIUtils;
import io.palaima.debugdrawer.view.DebugView;
import io.palaima.debugdrawer.view.ScrimInsetsFrameLayout;

public class DebugDrawer {

    private final DrawerLayout drawerLayout;

    private ScrollView sliderLayout;
    private DebugView debugView;

    private int drawerGravity;

    private DebugDrawer(Builder builder) {
        drawerLayout = builder.drawerLayout;
        drawerGravity = builder.drawerGravity;
        sliderLayout = builder.sliderLayout;
        debugView = builder.debugView;
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
     *
     * @return
     */
    public boolean isDrawerOpen() {
        if (drawerLayout != null && sliderLayout != null) {
            return drawerLayout.isDrawerOpen(sliderLayout);
        }
        return false;
    }

    /**
     * Enable or disable interaction with all drawers.
     *
     * @return
     */
    public void setDrawerLockMode(int lockMode) {
        if (drawerLayout != null && sliderLayout != null) {
            drawerLayout.setDrawerLockMode(lockMode);
        }
    }

    /**
     * Calls modules {@link DebugModule#onResume()} method
     */
    void onResume() {
        debugView.onResume();
    }

    /**
     * Calls modules {@link DebugModule#onPause()} method
     */
    void onPause() {
        debugView.onPause();
    }

    /**
     * Starts all modules and calls their {@link DebugModule#onStart()} method
     */
    void onStart() {
        debugView.onStart();
    }

    /**
     * Removes all modules and calls their {@link DebugModule#onStop()} method
     */
    void onStop() {
        debugView.onStop();
    }

    public static class Builder {

        private ViewGroup rootView;

        private Activity activity;

        private DrawerLayout drawerLayout;

        private ScrollView sliderLayout;

        private int drawerGravity = Gravity.END;

        //the width of the drawer
        private int drawerWidth = -1;

        private DebugModule[] drawerItems;

        private DrawerLayout.DrawerListener onDrawerListener;

        private int sliderBackgroundColor = 0;

        private int sliderBackgroundColorRes = -1;

        private Drawable sliderBackgroundDrawable;

        private int sliderBackgroundDrawableRes = -1;

        private int themeRes = -1;

        private DebugView debugView;

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
         * Pass the rootView as resource of the Drawer which will be used to inflate the
         * DrawerLayout in
         */
        public Builder rootView(int rootViewRes) {
            if (activity == null) {
                throw new RuntimeException("please pass an activity first to use this call");
            }

            return rootView((ViewGroup) activity.findViewById(rootViewRes));
        }

        /**
         * Set the gravity for the drawer. START, LEFT | RIGHT, END
         */
        public Builder gravity(int gravity) {
            this.drawerGravity = gravity;
            return this;
        }

        /**
         * Set the Drawer width with a pixel value
         */
        public Builder widthPx(int drawerWidthPx) {
            this.drawerWidth = drawerWidthPx;
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

        /**
         * Set the Drawer width with a dimension resource
         */
        public Builder widthRes(int drawerWidthRes) {
            if (activity == null) {
                throw new RuntimeException("please pass an activity first to use this call");
            }

            this.drawerWidth = activity.getResources().getDimensionPixelSize(drawerWidthRes);
            return this;
        }

        /**
         * Set the background color for the Slider.
         * This is the view containing the list.
         */
        public Builder backgroundColor(int sliderBackgroundColor) {
            this.sliderBackgroundColor = sliderBackgroundColor;
            return this;
        }

        /**
         * Set the background color for the Slider from a Resource.
         * This is the view containing the list.
         */
        public Builder backgroundColorRes(@IntegerRes int sliderBackgroundColorRes) {
            this.sliderBackgroundColorRes = sliderBackgroundColorRes;
            return this;
        }


        /**
         * Set the background drawable for the Slider.
         * This is the view containing the list.
         */
        public Builder backgroundDrawable(Drawable sliderBackgroundDrawable) {
            this.sliderBackgroundDrawable = sliderBackgroundDrawable;
            return this;
        }


        /**
         * Set the background drawable for the Slider from a Resource.
         * This is the view containing the list.
         */
        public Builder backgroundDrawableRes(@DrawableRes int sliderBackgroundDrawableRes) {
            this.sliderBackgroundDrawableRes = sliderBackgroundDrawableRes;
            return this;
        }

        public Builder setDrawerListener(DrawerLayout.DrawerListener onDrawerListener) {
            this.onDrawerListener = onDrawerListener;
            return this;
        }

        public Builder withTheme(@StyleRes int themeRes) {
            this.themeRes = themeRes;
            return this;
        }

        /**
         * Add a initial DrawerItem or a DrawerItem Array for the Drawer
         */
        public Builder modules(DebugModule... drawerItems) {
            this.drawerItems = drawerItems;
            return this;
        }

        /**
         * Build and add the Drawer to your activity
         *
         * @return
         */
        public DebugDrawer build() {
            if (activity == null) {
                throw new RuntimeException("please pass an activity");
            }

            if (rootView == null || rootView.getChildCount() == 0) {
                throw new RuntimeException(
                    "You have to set your layout for this activity with setContentView() first.");
            }

            LayoutInflater layoutInflater = activity.getLayoutInflater();
            if (themeRes > 0) {
                layoutInflater = layoutInflater.cloneInContext(new ContextThemeWrapper(activity, themeRes));
            }
            drawerLayout = (DrawerLayout) layoutInflater
                .inflate(R.layout.dd_debug_drawer, rootView, false);

            final boolean alreadyInflated = rootView.getChildAt(0).getId() == R.id.dd_drawer_layout;

            //store the original content views
            final List<View> originalContentViews = new ArrayList<>(rootView.getChildCount());
            for (int i = 0; i < rootView.getChildCount(); i++) {
                originalContentViews.add(rootView.getChildAt(i));
            }

            //get the drawer root
            drawerContentRoot = (ScrimInsetsFrameLayout) drawerLayout.getChildAt(0);

            //only add the new layout if it wasn't done before
            if (!alreadyInflated) {
                // remove the contentView
                for (View contentView : originalContentViews) {
                    rootView.removeView(contentView);
                }
            } else {
                //if it was already inflated we have to clean up again
                rootView.removeAllViews();
            }

            //create the layoutParams to use for the contentView
            final FrameLayout.LayoutParams layoutParamsContentView = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            );

            //add the original content Views to the drawer content frameLayout
            for (View contentView : originalContentViews) {
                drawerContentRoot.addView(contentView, layoutParamsContentView);
            }

            //add the drawerLayout to the root
            rootView.addView(drawerLayout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ));

            final DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                    if (onDrawerListener != null) {
                        onDrawerListener.onDrawerSlide(drawerView, slideOffset);
                    }
                }

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {
                    if (onDrawerListener != null) {
                        onDrawerListener.onDrawerOpened(drawerView);
                    }
                    if (drawerItems != null && drawerItems.length != 0) {
                        for (DebugModule drawerItem : drawerItems) {
                            drawerItem.onOpened();
                        }
                    }
                }

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {
                    if (onDrawerListener != null) {
                        onDrawerListener.onDrawerClosed(drawerView);
                    }
                    if (drawerItems != null && drawerItems.length != 0) {
                        for (DebugModule drawerItem : drawerItems) {
                            drawerItem.onClosed();
                        }
                    }
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            };

            rootView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

                @Override
                public void onViewAttachedToWindow(View view) {
                    if (drawerLayout != null) {
                        drawerLayout.addDrawerListener(listener);
                    }
                }

                @Override
                public void onViewDetachedFromWindow(View view) {
                    if (drawerLayout != null) {
                        drawerLayout.removeDrawerListener(listener);
                    }
                    if (rootView != null) {
                        rootView.removeOnAttachStateChangeListener(this);
                    }
                }
            });

            sliderLayout = drawerLayout.findViewById(R.id.dd_slider_layout);
            debugView = sliderLayout.findViewById(R.id.dd_debug_view);

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

            // set the background
            if (sliderBackgroundColor != 0) {
                sliderLayout.setBackgroundColor(sliderBackgroundColor);
            } else if (sliderBackgroundColorRes != -1) {
                sliderLayout.setBackgroundColor(activity.getResources().getColor(sliderBackgroundColorRes));
            } else if (sliderBackgroundDrawable != null) {
                UIUtils.setBackground(sliderLayout, sliderBackgroundDrawable);
            } else if (sliderBackgroundDrawableRes != -1) {
                UIUtils.setBackground(sliderLayout, sliderBackgroundColorRes);
            }

            debugView.modules(drawerItems);

            //create the result object
            final DebugDrawer result = new DebugDrawer(this);

            //register a lifecycle callback for start, stop, pause and resume events
            activity.getApplication().registerActivityLifecycleCallbacks(new DebugDrawerLifecycleCallbacks(activity, result));

            //forget the reference to the activity
            activity = null;


            return result;
        }

        /**
         * helper to extend the layoutParams of the drawer
         *
         * @param params
         * @return
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
