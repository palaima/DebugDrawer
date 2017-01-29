/*
 * Copyright (C) 2014 LemonLabs
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

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import io.palaima.debugdrawer.base.DebugModuleAdapter;

public class SettingsModule extends DebugModuleAdapter implements View.OnClickListener {

    private View      developerTitle;
    private ImageView developer;
    private View      batteryTitle;
    private ImageView battery;
    private View      settingsTitle;
    private ImageView settings;
    private View      infoTitle;
    private ImageView info;
    private View      uninstallTitle;
    private ImageView uninstall;
    private View      locationTitle;
    private ImageView location;

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final View view = inflater.inflate(R.layout.dd_debug_drawer_module_settings, parent, false);
        view.setClickable(false);
        view.setEnabled(false);

        developer = (ImageView) view.findViewById(R.id.dd_debug_settings_developer);
        developerTitle = view.findViewById(R.id.dd_debug_settings_developer_title);
        battery = (ImageView) view.findViewById(R.id.dd_debug_settings_batery);
        batteryTitle = view.findViewById(R.id.dd_debug_settings_batery_title);
        settings = (ImageView) view.findViewById(R.id.dd_debug_settings_settings);
        settingsTitle = view.findViewById(R.id.dd_debug_settings_settings_title);
        info = (ImageView) view.findViewById(R.id.dd_debug_settings_info);
        infoTitle = view.findViewById(R.id.dd_debug_settings_info_title);
        uninstall = (ImageView) view.findViewById(R.id.dd_debug_settings_delete);
        uninstallTitle = view.findViewById(R.id.dd_debug_settings_delete_title);
        location = (ImageView) view.findViewById(R.id.dd_debug_location_settings);
        locationTitle = view.findViewById(R.id.dd_debug_location_settings_title);

        developer.setOnClickListener(this);
        developerTitle.setOnClickListener(this);
        battery.setOnClickListener(this);
        batteryTitle.setOnClickListener(this);
        settings.setOnClickListener(this);
        settingsTitle.setOnClickListener(this);
        info.setOnClickListener(this);
        infoTitle.setOnClickListener(this);
        uninstall.setOnClickListener(this);
        uninstallTitle.setOnClickListener(this);
        location.setOnClickListener(this);
        locationTitle.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();
        if (v == developer || v == developerTitle) {
            // open dev settings
            Intent devIntent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            devIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(devIntent, 0);
            if (resolveInfo != null) context.startActivity(devIntent);
            else Toast.makeText(context, "Developer settings not available on device",
                Toast.LENGTH_SHORT).show();
        } else if (v == battery || v == batteryTitle) {
            // try to find an app to handle battery settings
            Intent batteryIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
            batteryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(batteryIntent, 0);
            if (resolveInfo != null) context.startActivity(batteryIntent);
            else Toast.makeText(context, "No app found to handle power usage intent", Toast.LENGTH_SHORT).show();
        } else if (v == settings || v == settingsTitle) {
            // open android settings
            Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(settingsIntent);
        } else if (v == info || v == infoTitle) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } else if (v == uninstall || v == uninstallTitle) {
            // open dialog to uninstall app
            Uri packageURI = Uri.parse("package:" + context.getPackageName());
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            context.startActivity(uninstallIntent);
        } else if (v == location || v == locationTitle) {
            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(locationIntent);
        }
    }
}
