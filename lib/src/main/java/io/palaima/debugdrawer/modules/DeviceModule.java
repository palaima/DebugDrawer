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

package io.palaima.debugdrawer.modules;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import io.palaima.debugdrawer.DebugWidgets;
import io.palaima.debugdrawer.DebugModule;


/**
 * http://blog.csdn.net/vennl/article/details/7078738
 */
public class DeviceModule implements DebugModule {

    private String deviceMake;

    private String deviceModel;

    private String deviceResolution;

    private String deviceDensity;

    private String systemVersion;

    private String androidApi;

    public DeviceModule(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        String densityBucket = getDensityString(displayMetrics);
        deviceMake = truncateAt(Build.MANUFACTURER, 20);
        deviceModel = truncateAt(Build.MODEL, 20);
        deviceResolution = displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
        deviceDensity = displayMetrics.densityDpi + "dpi (" + densityBucket + ")";
        systemVersion = Build.VERSION.RELEASE;
        androidApi = String.valueOf(Build.VERSION.SDK_INT);
    }

    @NonNull
    @Override
    public String getName() {
        return "Device Info";
    }

    @Override
    public DebugWidgets createWidgets(DebugWidgets.DebugWidgetsBuilder builder) {
        return builder.addText("OEM", deviceMake)
                .addText("Device Model", deviceModel)
                .addText("Product", Build.PRODUCT)
                .addText("Resolution", deviceResolution)
                .addText("Density", deviceDensity)
                .addText("System Version", systemVersion)
                .addText("Android API", androidApi)
                .build();
    }

    public static String truncateAt(String string, int length) {
        return string.length() > length ? string.substring(0, length) : string;
    }

    private static String getDensityString(DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return String.valueOf(displayMetrics.densityDpi);
        }
    }

}
