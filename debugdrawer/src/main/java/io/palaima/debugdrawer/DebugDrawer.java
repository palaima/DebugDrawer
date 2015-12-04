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
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Collections;

import io.palaima.debugdrawer.module.DrawerModule;
import io.palaima.debugdrawer.util.UIUtils;
import io.palaima.debugdrawer.view.ScrimInsetsFrameLayout;

public class DebugDrawer {

    private final DrawerLayout mDrawerLayout;

    private ScrollView mSliderLayout;

    private int mDrawerGravity;

    private final ArrayList<DrawerModule> mDrawerItems;


    private DebugDrawer(Builder builder) {
        mDrawerLayout = builder.mDrawerLayout;
        mDrawerGravity = builder.mDrawerGravity;
        mDrawerItems = builder.mDrawerItems;
        mSliderLayout = builder.mSliderLayout;
    }

    /**
     * Open the drawer
     */
    public void openDrawer() {
        if (mDrawerLayout != null && mSliderLayout != null) {
            if (mDrawerGravity != 0) {
                mDrawerLayout.openDrawer(mDrawerGravity);
            } else {
                mDrawerLayout.openDrawer(mSliderLayout);
            }
        }
    }

    /**
     * close the drawer
     */
    public void closeDrawer() {
        if (mDrawerLayout != null) {
            if (mDrawerGravity != 0) {
                mDrawerLayout.closeDrawer(mDrawerGravity);
            } else {
                mDrawerLayout.closeDrawer(mSliderLayout);
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
        if (mDrawerLayout != null && mSliderLayout != null) {
            return mDrawerLayout.isDrawerOpen(mSliderLayout);
        }
        return false;
    }

    /**
     * Starts all modules and calls their {@link DrawerModule#onStart()} method
     */
    public void onStart() {
        for (DrawerModule drawerItem : mDrawerItems) {
            drawerItem.onStart();
        }
    }

    /**
     * Removes all modules and calls their {@link DrawerModule#onStop()} method
     */
    public void onStop() {
        for (DrawerModule drawerItem : mDrawerItems) {
            drawerItem.onStop();
        }
    }

    public static class Builder {

        private ViewGroup mRootView;

        private Activity mActivity;

        private DrawerLayout mDrawerLayout;

        private ScrollView mSliderLayout;

        private int mDrawerGravity = Gravity.END;

        //the width of the drawer
        private int mDrawerWidth = -1;

        private ArrayList<DrawerModule> mDrawerItems;

        private DrawerLayout.DrawerListener mOnDrawerListener;

        private int mSliderBackgroundColor = 0;

        private int mSliderBackgroundColorRes = -1;

        private Drawable mSliderBackgroundDrawable;

        private int mSliderBackgroundDrawableRes = -1;

        private LinearLayout mContainer;

        private ScrimInsetsFrameLayout mDrawerContentRoot;

        /**
         * Pass the activity you use the drawer in ;)
         * This is required if you want to set any values by resource
         */
        public Builder(Activity activity) {
            this.mRootView = (ViewGroup) activity.findViewById(android.R.id.content);
            this.mActivity = activity;
        }

        /**
         * Pass the rootView of the Drawer which will be used to inflate the DrawerLayout in
         */
        public Builder rootView(ViewGroup rootView) {
            this.mRootView = rootView;
            return this;
        }

        /**
         * Pass the rootView as resource of the Drawer which will be used to inflate the
         * DrawerLayout in
         */
        public Builder rootView(int rootViewRes) {
            if (mActivity == null) {
                throw new RuntimeException("please pass an activity first to use this call");
            }

            return rootView((ViewGroup) mActivity.findViewById(rootViewRes));
        }

        /**
         * Set the gravity for the drawer. START, LEFT | RIGHT, END
         */
        public Builder gravity(int gravity) {
            this.mDrawerGravity = gravity;
            return this;
        }

        /**
         * Set the Drawer width with a pixel value
         */
        public Builder widthPx(int drawerWidthPx) {
            this.mDrawerWidth = drawerWidthPx;
            return this;
        }

        /**
         * Set the Drawer width with a dp value
         */
        public Builder widthDp(int drawerWidthDp) {
            if (mActivity == null) {
                throw new RuntimeException("please pass an activity first to use this call");
            }
            this.mDrawerWidth = (int) TypedValue.applyDimension(1, drawerWidthDp,
                    mActivity.getResources().getDisplayMetrics());
            return this;
        }

        /**
         * Set the Drawer width with a dimension resource
         */
        public Builder widthRes(int drawerWidthRes) {
            if (mActivity == null) {
                throw new RuntimeException("please pass an activity first to use this call");
            }

            this.mDrawerWidth = mActivity.getResources().getDimensionPixelSize(drawerWidthRes);
            return this;
        }

        /**
         * Set the background color for the Slider.
         * This is the view containing the list.
         */
        public Builder backgroundColor(int sliderBackgroundColor) {
            this.mSliderBackgroundColor = sliderBackgroundColor;
            return this;
        }

        /**
         * Set the background color for the Slider from a Resource.
         * This is the view containing the list.
         */
        public Builder backgroundColorRes(@IntegerRes int sliderBackgroundColorRes) {
            this.mSliderBackgroundColorRes = sliderBackgroundColorRes;
            return this;
        }


        /**
         * Set the background drawable for the Slider.
         * This is the view containing the list.
         */
        public Builder backgroundDrawable(Drawable sliderBackgroundDrawable) {
            this.mSliderBackgroundDrawable = sliderBackgroundDrawable;
            return this;
        }


        /**
         * Set the background drawable for the Slider from a Resource.
         * This is the view containing the list.
         */
        public Builder backgroundDrawableRes(@DrawableRes int sliderBackgroundDrawableRes) {
            this.mSliderBackgroundDrawableRes = sliderBackgroundDrawableRes;
            return this;
        }

        /**
         * Add a initial DrawerItem or a DrawerItem Array  for the Drawer
         */
        public Builder modules(DrawerModule... drawerItems) {
            if (this.mDrawerItems == null) {
                this.mDrawerItems = new ArrayList<>();
            }

            if (drawerItems != null) {
                Collections.addAll(this.mDrawerItems, drawerItems);
            }
            return this;
        }

        /**
         * Build and add the Drawer to your activity
         *
         * @return
         */
        public DebugDrawer build() {
            if (mActivity == null) {
                throw new RuntimeException("please pass an activity");
            }

            if (mRootView == null || mRootView.getChildCount() == 0) {
                throw new RuntimeException(
                        "You have to set your layout for this activity with setContentView() first.");
            }

            //create the layoutParams to use for the contentView
            FrameLayout.LayoutParams layoutParamsContentView = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );

            // if activity's layout is just merged using <merge> we should add
            // an intermediate frame saving original layout params
            // (and it's ok if content is merged with just one view in it - just skip frame injection)
            final boolean isActivityContentMerged = mRootView.getChildCount() > 1;
            if (isActivityContentMerged) {
                final int originalCount = mRootView.getChildCount();
                final View[] children = new View[originalCount];
                for (int i = 0; i < originalCount; i++) {
                    children[i] = mRootView.getChildAt(i);
                }
                final FrameLayout frame = new FrameLayout(mActivity);
                mRootView.removeAllViews();
                for (int i = 0; i < originalCount; i++) {
                    final View child = children[i];
                    frame.addView(child, i, child.getLayoutParams());
                }
                mRootView.addView(frame, layoutParamsContentView);
            }

            //get the content view
            View contentView = mRootView.getChildAt(0);
            boolean alreadyInflated = contentView instanceof DrawerLayout;

            //only add the new layout if it wasn't done before
            if (alreadyInflated) {
                //if it was already inflated we have to clean up again
                mRootView.removeAllViews();
            } else {
                // remove the contentView
                mRootView.removeView(contentView);
            }

            mDrawerLayout = (DrawerLayout) mActivity.getLayoutInflater()
                    .inflate(R.layout.debug_drawer, mRootView, false);

            //add the drawerLayout to the root
            mRootView.addView(mDrawerLayout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            //get the drawer root
            mDrawerContentRoot = (ScrimInsetsFrameLayout) mDrawerLayout.getChildAt(0);

            //add the contentView to the drawer content frameLayout
            mDrawerContentRoot.addView(contentView, layoutParamsContentView);

            mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerSlide(drawerView, slideOffset);
                    }
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerOpened(drawerView);
                    }
                    if (mDrawerItems != null && !mDrawerItems.isEmpty()) {
                        for (DrawerModule drawerItem : mDrawerItems) {
                            drawerItem.onOpened();
                        }
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    if (mOnDrawerListener != null) {
                        mOnDrawerListener.onDrawerClosed(drawerView);
                    }
                    if (mDrawerItems != null && !mDrawerItems.isEmpty()) {
                        for (DrawerModule drawerItem : mDrawerItems) {
                            drawerItem.onClosed();
                        }
                    }
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });


            mSliderLayout = (ScrollView) mActivity.getLayoutInflater().inflate(
                    R.layout.debug_drawer_slider, mDrawerLayout, false);
            mContainer = (LinearLayout) mSliderLayout.findViewById(R.id.container);

            // get the layout params
            DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mSliderLayout.getLayoutParams();
            if (params != null) {
                // if we've set a custom gravity set it
                if (mDrawerGravity != 0) {
                    params.gravity = mDrawerGravity;
                }
                // if this is a drawer from the right, change the margins :D
                params = processDrawerLayoutParams(params);
                // set the new layout params
                mSliderLayout.setLayoutParams(params);
            }

            // set the background
            if (mSliderBackgroundColor != 0) {
                mSliderLayout.setBackgroundColor(mSliderBackgroundColor);
            } else if (mSliderBackgroundColorRes != -1) {
                mSliderLayout.setBackgroundColor(mActivity.getResources().getColor(mSliderBackgroundColorRes));
            } else if (mSliderBackgroundDrawable != null) {
                UIUtils.setBackground(mSliderLayout, mSliderBackgroundDrawable);
            } else if (mSliderBackgroundDrawableRes != -1) {
                UIUtils.setBackground(mSliderLayout, mSliderBackgroundColorRes);
            }

            LayoutInflater inflater = LayoutInflater.from(mActivity);
            if (mDrawerItems != null && !mDrawerItems.isEmpty()) {
                DrawerModule drawerItem;
                for (int i = 0; i < mDrawerItems.size(); i++) {
                    drawerItem = mDrawerItems.get(i);
                    mContainer.addView(drawerItem.onCreateView(inflater, mContainer), i);
                }
            }

            mDrawerLayout.addView(mSliderLayout, 1);

            //create the result object
            DebugDrawer result = new DebugDrawer(this);

            //forget the reference to the activity
            mActivity = null;


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
                if (mDrawerGravity != 0 && (mDrawerGravity == Gravity.RIGHT || mDrawerGravity == Gravity.END)) {
                    params.rightMargin = 0;
                    if (Build.VERSION.SDK_INT >= 17) {
                        params.setMarginEnd(0);
                    }

                    params.leftMargin = mActivity.getResources().getDimensionPixelSize(R.dimen.debug_drawer_margin);
                    if (Build.VERSION.SDK_INT >= 17) {
                        params.setMarginEnd(mActivity.getResources().getDimensionPixelSize(R.dimen.debug_drawer_margin));
                    }
                }

                if (mDrawerWidth > -1) {
                    params.width = mDrawerWidth;
                } else {
                    params.width = UIUtils.getOptimalDrawerWidth(mActivity);
                }
            }

            return params;
        }
    }
}
