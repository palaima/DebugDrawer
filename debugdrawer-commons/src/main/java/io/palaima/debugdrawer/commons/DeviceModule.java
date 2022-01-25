/*
 * Copyright (C) 2015 Mantas Palaima
 * Copyright (C) 2016 Oleg Godovykh
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

import android.os.Build;
import androidx.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.palaima.debugdrawer.base.DebugModuleAdapter;

public class DeviceModule extends DebugModuleAdapter {

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final View view = inflater.inflate(R.layout.dd_debug_drawer_module_device, parent, false);
        view.setClickable(false);
        view.setEnabled(false);

        final TextView deviceMakeLabel = view.findViewById(R.id.dd_debug_device_make);
        final TextView deviceModelLabel = view.findViewById(R.id.dd_debug_device_model);
        final TextView deviceResolutionLabel = view.findViewById(R.id.dd_debug_device_resolution);
        final TextView deviceDensityLabel = view.findViewById(R.id.dd_debug_device_density);
        final TextView deviceReleaseLabel = view.findViewById(R.id.dd_debug_device_release);
        final TextView deviceApiLabel = view.findViewById(R.id.dd_debug_device_api);

        final DisplayMetrics displayMetrics = parent.getContext().getResources().getDisplayMetrics();
        final String densityBucket = getDensityString(displayMetrics);
        final String deviceMake = truncateAt(Build.MANUFACTURER, 20);
        final String deviceModel = truncateAt(Build.MODEL, 20);
        final String deviceResolution = displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
        final String deviceDensity = displayMetrics.densityDpi + "dpi (" + densityBucket + ")";
        final String deviceRelease = Build.VERSION.RELEASE;
        final String deviceApi = String.valueOf(Build.VERSION.SDK_INT);

        deviceModelLabel.setText(deviceModel);
        deviceMakeLabel.setText(deviceMake);
        deviceResolutionLabel.setText(deviceResolution);
        deviceDensityLabel.setText(deviceDensity);
        deviceApiLabel.setText(deviceApi);
        deviceReleaseLabel.setText(deviceRelease);

        return view;
    }

    private static String truncateAt(String string, int length) {
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
