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

package io.palaima.debugdrawer.commons;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.palaima.debugdrawer.base.DebugModule;

public class DeviceModule implements DebugModule {

    private String deviceMake;
    private String deviceModel;
    private String deviceResolution;
    private String deviceDensity;
    private String deviceRelease;
    private String deviceApi;

    private TextView deviceMakeLabel;
    private TextView deviceModelLabel;
    private TextView deviceResolutionLabel;
    private TextView deviceDensityLabel;
    private TextView deviceReleaseLabel;
    private TextView deviceApiLabel;

    public DeviceModule(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        String densityBucket = getDensityString(displayMetrics);
        deviceMake = truncateAt(Build.MANUFACTURER, 20);
        deviceModel = truncateAt(Build.MODEL, 20);
        deviceResolution = displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
        deviceDensity = displayMetrics.densityDpi + "dpi (" + densityBucket + ")";
        deviceRelease = Build.VERSION.RELEASE;
        deviceApi = String.valueOf(Build.VERSION.SDK_INT);
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

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.dd_debug_drawer_module_device, parent, false);
        view.setClickable(false);
        view.setEnabled(false);

        deviceMakeLabel = (TextView) view.findViewById(R.id.dd_debug_device_make);
        deviceModelLabel = (TextView) view.findViewById(R.id.dd_debug_device_model);
        deviceResolutionLabel = (TextView) view.findViewById(R.id.dd_debug_device_resolution);
        deviceDensityLabel = (TextView) view.findViewById(R.id.dd_debug_device_density);
        deviceReleaseLabel = (TextView) view.findViewById(R.id.dd_debug_device_release);
        deviceApiLabel = (TextView) view.findViewById(R.id.dd_debug_device_api);

        deviceModelLabel.setText(deviceModel);
        deviceMakeLabel.setText(deviceMake);
        deviceResolutionLabel.setText(deviceResolution);
        deviceDensityLabel.setText(deviceDensity);
        deviceApiLabel.setText(deviceApi);
        deviceReleaseLabel.setText(deviceRelease);

        return view;
    }

    @Override
    public void onOpened() {

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
