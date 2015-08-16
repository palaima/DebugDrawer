/*
 * Copyright (C) 2014 LemonLabs
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
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import io.palaima.debugdrawer.R;

public class SettingsModule implements DrawerModule, View.OnClickListener {

    private final Context mContext;

    private View mDeveloperTitle;
    private ImageView mDeveloper;
    private View mBatteryTitle;
    private ImageView mBattery;
    private View mSettingsTitle;
    private ImageView mSettings;
    private View mInfoTitle;
    private ImageView mInfo;
    private View mUninstallTitle;
    private ImageView mUninstall;
    private View mLocationTitle;
    private ImageView mLocation;

    public SettingsModule(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.debug_drawer_module_settings, parent, false);
        view.setClickable(false);
        view.setEnabled(false);

        mDeveloper = (ImageView) view.findViewById(R.id.debug_settings_developer);
        mDeveloperTitle = view.findViewById(R.id.debug_settings_developer_title);
        mBattery = (ImageView) view.findViewById(R.id.debug_settings_batery);
        mBatteryTitle = view.findViewById(R.id.debug_settings_batery_title);
        mSettings = (ImageView) view.findViewById(R.id.debug_settings_settings);
        mSettingsTitle = view.findViewById(R.id.debug_settings_settings_title);
        mInfo = (ImageView) view.findViewById(R.id.debug_settings_info);
        mInfoTitle = view.findViewById(R.id.debug_settings_info_title);
        mUninstall = (ImageView) view.findViewById(R.id.debug_settings_delete);
        mUninstallTitle = view.findViewById(R.id.debug_settings_delete_title);
        mLocation = (ImageView) view.findViewById(R.id.debug_location_settings);
        mLocationTitle = view.findViewById(R.id.debug_location_settings_title);

        mDeveloper.setOnClickListener(this);
        mDeveloperTitle.setOnClickListener(this);
        mBattery.setOnClickListener(this);
        mBatteryTitle.setOnClickListener(this);
        mSettings.setOnClickListener(this);
        mSettingsTitle.setOnClickListener(this);
        mInfo.setOnClickListener(this);
        mInfoTitle.setOnClickListener(this);
        mUninstall.setOnClickListener(this);
        mUninstallTitle.setOnClickListener(this);
        mLocation.setOnClickListener(this);
        mLocationTitle.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v == mDeveloper || v == mDeveloperTitle) {
            // open dev settings
            Intent devIntent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            ResolveInfo resolveInfo = mContext.getPackageManager().resolveActivity(devIntent, 0);
            if (resolveInfo != null) mContext.startActivity(devIntent);
            else Toast.makeText(mContext, "Developer settings not available on device",
                Toast.LENGTH_SHORT).show();
        } else if (v == mBattery || v == mBatteryTitle) {
            // try to find an app to handle battery settings
            Intent batteryIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
            ResolveInfo resolveInfo = mContext.getPackageManager().resolveActivity(batteryIntent, 0);
            if (resolveInfo != null) mContext.startActivity(batteryIntent);
            else Toast.makeText(mContext, "No app found to handle power usage intent", Toast.LENGTH_SHORT).show();
        } else if (v == mSettings || v == mSettingsTitle) {
            // open android settings
            mContext.startActivity(new Intent(Settings.ACTION_SETTINGS));
        } else if (v == mInfo || v == mInfoTitle) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
            mContext.startActivity(intent);
        } else if (v == mUninstall || v == mUninstallTitle) {
            // open dialog to uninstall app
            Uri packageURI = Uri.parse("package:" + mContext.getPackageName());
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            mContext.startActivity(uninstallIntent);
        } else if (v == mLocation || v == mLocationTitle) {
            mContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
}
