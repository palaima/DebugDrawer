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

package io.palaima.debugdrawer.module;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.palaima.debugdrawer.R;

public class DeviceModule implements DrawerModule {

    private String mDeviceMake;
    private String mDeviceModel;
    private String mDeviceResolution;
    private String mDeviceDensity;
    private String mDeviceRelease;
    private String mDeviceApi;

    private TextView mDeviceMakeView;
    private TextView mDeviceModelView;
    private TextView mDeviceResolutionView;
    private TextView mDeviceDensityView;
    private TextView mDeviceReleaseView;
    private TextView mDeviceApiView;

    public DeviceModule(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        String densityBucket = getDensityString(displayMetrics);
        mDeviceMake = truncateAt(Build.MANUFACTURER, 20);
        mDeviceModel = truncateAt(Build.MODEL, 20);
        mDeviceResolution = displayMetrics.heightPixels + "x" + displayMetrics.widthPixels;
        mDeviceDensity = displayMetrics.densityDpi + "dpi (" + densityBucket + ")";
        mDeviceRelease = Build.VERSION.RELEASE;
        mDeviceApi = String.valueOf(Build.VERSION.SDK_INT);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.debug_drawer_module_device, parent, false);
        view.setClickable(false);
        view.setEnabled(false);

        mDeviceMakeView = (TextView) view.findViewById(R.id.debug_device_make);
        mDeviceModelView = (TextView) view.findViewById(R.id.debug_device_model);
        mDeviceResolutionView = (TextView) view.findViewById(R.id.debug_device_resolution);
        mDeviceDensityView = (TextView) view.findViewById(R.id.debug_device_density);
        mDeviceReleaseView = (TextView) view.findViewById(R.id.debug_device_release);
        mDeviceApiView = (TextView) view.findViewById(R.id.debug_device_api);

        mDeviceModelView.setText(mDeviceModel);
        mDeviceMakeView.setText(mDeviceMake);
        mDeviceResolutionView.setText(mDeviceResolution);
        mDeviceDensityView.setText(mDeviceDensity);
        mDeviceApiView.setText(mDeviceApi);
        mDeviceReleaseView.setText(mDeviceRelease);

        return view;
    }

    @Override
    public void onOpened() {

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
