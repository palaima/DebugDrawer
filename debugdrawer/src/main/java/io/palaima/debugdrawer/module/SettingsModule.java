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

    private View mRootView;
    private ImageView mDeveloper;
    private ImageView mBattery;
    private ImageView mSettings;
    private ImageView mInfo;
    private ImageView mUninstall;

    public SettingsModule(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.debug_drawer_module_settings, parent, false);

        mRootView = view;
        mDeveloper = (ImageView) view.findViewById(R.id.debug_settings_developer);
        mBattery = (ImageView) view.findViewById(R.id.debug_settings_batery);
        mSettings = (ImageView) view.findViewById(R.id.debug_settings_settings);
        mInfo = (ImageView) view.findViewById(R.id.debug_settings_info);
        mUninstall = (ImageView) view.findViewById(R.id.debug_settings_delete);

        mRootView.setClickable(false);
        mRootView.setEnabled(false);
        mDeveloper.setOnClickListener(this);
        mBattery.setOnClickListener(this);
        mSettings.setOnClickListener(this);
        mInfo.setOnClickListener(this);
        mUninstall.setOnClickListener(this);

        return view;
    }

    @Override
    public void onRefreshView() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.debug_settings_developer) {
            // open dev settings
            Intent devIntent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            ResolveInfo resolveInfo = mContext.getPackageManager().resolveActivity(devIntent, 0);
            if (resolveInfo != null) mContext.startActivity(devIntent);
            else Toast.makeText(mContext, "Developer settings not available on device",
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.debug_settings_batery) {
            // try to find an app to handle battery settings
            Intent batteryIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
            ResolveInfo resolveInfo = mContext.getPackageManager().resolveActivity(batteryIntent, 0);
            if (resolveInfo != null) mContext.startActivity(batteryIntent);
            else Toast.makeText(mContext, "No app found to handle power usage intent", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.debug_settings_settings) {
            // open android settings
            mContext.startActivity(new Intent(Settings.ACTION_SETTINGS));
        } else if (id == R.id.debug_settings_info) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + mContext.getPackageName()));
            mContext.startActivity(intent);
        } else if (id == R.id.debug_settings_delete) {
            // open dialog to uninstall app
            Uri packageURI = Uri.parse("package:" + mContext.getPackageName());
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            mContext.startActivity(uninstallIntent);
        }
    }
}
