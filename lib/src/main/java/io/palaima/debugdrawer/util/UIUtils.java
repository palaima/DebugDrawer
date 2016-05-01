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

package io.palaima.debugdrawer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

import io.palaima.debugdrawer.R;

@SuppressLint("InlinedApi")
public class UIUtils {

    /**
     * Returns the screen width in pixels
     *
     * @param context is the context to get the resources
     * @return the screen width in pixels
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * helper to calculate the optimal drawer width
     */
    public static int getOptimalDrawerWidth(Context context) {
        int possibleMinDrawerWidth = UIUtils.getScreenWidth(context) - UIUtils.getActionBarHeight(context);
        int maxDrawerWidth = context.getResources().getDimensionPixelSize(
                R.dimen.dd_debug_drawer_width);
        return Math.min(possibleMinDrawerWidth, maxDrawerWidth);
    }

    /**
     * helper to calculate the actionBar height
     */
    public static int getActionBarHeight(Context context) {
        int actionBarHeight = UIUtils.getThemeAttributeDimensionSize(context,
                android.R.attr.actionBarSize);
        // FIXME: 2016/3/25 R.dimen.abc_action_bar_default_height_material is private in android framework
        /*if (actionBarHeight == 0) {
            actionBarHeight = context.getResources()
                    .getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
        }*/
        return actionBarHeight;
    }

    /**
     * Returns the size in pixels of an attribute dimension
     *
     * @param context the context to get the resources from
     * @param attr    is the attribute dimension we want to know the size from
     * @return the size in pixels of an attribute dimension
     */
    public static int getThemeAttributeDimensionSize(Context context, int attr) {
        TypedArray a = null;
        try {
            a = context.getTheme().obtainStyledAttributes(new int[]{attr});
            return a.getDimensionPixelSize(0, 0);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }
}
