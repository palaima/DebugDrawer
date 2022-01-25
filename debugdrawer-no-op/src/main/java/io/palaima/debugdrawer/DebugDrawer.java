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
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StyleRes;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.ViewGroup;

import io.palaima.debugdrawer.base.DebugModule;

public class DebugDrawer {
    private static final DebugDrawer INSTANCE = new DebugDrawer(null);

    private DebugDrawer(Builder builder) {

    }

    /**
     * Open the drawer
     */
    public void openDrawer() {

    }

    /**
     * close the drawer
     */
    public void closeDrawer() {

    }

    /**
     * Get the current state of the drawer.
     * True if the drawer is currently open.
     *
     * @return
     */
    public boolean isDrawerOpen() {
        return false;
    }

    /**
     * Enable or disable interaction with all drawers.
     */
    public void setDrawerLockMode(int lockMode) {

    }

    /**
     * Calls modules {@link DebugModule#onResume()} method
     */
    void onResume() {

    }

    /**
     * Calls modules {@link DebugModule#onPause()} method
     */
    void onPause() {

    }

    /**
     * Starts all modules and calls their {@link DebugModule#onStart()} method
     */
    void onStart() {

    }

    /**
     * Removes all modules and calls their {@link DebugModule#onStop()} method
     */
    void onStop() {

    }

    public static class Builder {

        /**
         * Pass the activity you use the drawer in ;)
         * This is required if you want to set any values by resource
         */
        public Builder(Activity activity) {

        }

        /**
         * Pass the rootView of the Drawer which will be used to inflate the DrawerLayout in
         */
        public Builder rootView(ViewGroup rootView) {
            return this;
        }

        /**
         * Pass the rootView as resource of the Drawer which will be used to inflate the
         * DrawerLayout in
         */
        public Builder rootView(int rootViewRes) {
            return this;
        }

        /**
         * Set the gravity for the drawer. START, LEFT | RIGHT, END
         */
        public Builder gravity(int gravity) {
            return this;
        }

        /**
         * Set the Drawer width with a pixel value
         */
        public Builder widthPx(int drawerWidthPx) {
            return this;
        }

        /**
         * Set the Drawer width with a dp value
         */
        public Builder widthDp(int drawerWidthDp) {
            return this;
        }

        /**
         * Set the Drawer width with a dimension resource
         */
        public Builder widthRes(int drawerWidthRes) {
            return this;
        }

        /**
         * Set the background color for the Slider.
         * This is the view containing the list.
         */
        public Builder backgroundColor(int sliderBackgroundColor) {
            return this;
        }

        /**
         * Set the background color for the Slider from a Resource.
         * This is the view containing the list.
         */
        public Builder backgroundColorRes(@IntegerRes int sliderBackgroundColorRes) {
            return this;
        }


        /**
         * Set the background drawable for the Slider.
         * This is the view containing the list.
         */
        public Builder backgroundDrawable(Drawable sliderBackgroundDrawable) {
            return this;
        }


        /**
         * Set the background drawable for the Slider from a Resource.
         * This is the view containing the list.
         */
        public Builder backgroundDrawableRes(@DrawableRes int sliderBackgroundDrawableRes) {
            return this;
        }

        public Builder setDrawerListener(DrawerLayout.DrawerListener onDrawerListener) {
            return this;
        }

        public Builder withTheme(@StyleRes int themeRes) {
            return this;
        }

        /**
         * Add a initial DrawerItem or a DrawerItem Array  for the Drawer
         */
        public Builder modules(DebugModule... drawerItems) {
            return this;
        }

        /**
         * Build and add the Drawer to your activity
         *
         * @return
         */
        public DebugDrawer build() {
            return DebugDrawer.INSTANCE;
        }
    }
}
