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
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import io.palaima.debugdrawer.base.DebugModuleAdapter;

public class SettingsModule extends DebugModuleAdapter implements View.OnClickListener {

    private LinearLayout developer;
    private LinearLayout battery;
    private LinearLayout settings;
    private LinearLayout info;
    private LinearLayout uninstall;
    private LinearLayout location;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final View view = inflater.inflate(R.layout.dd_debug_drawer_module_settings, parent, false);
        view.setClickable(false);
        view.setEnabled(false);

        developer = view.findViewById(R.id.dd_debug_settings_developer);
        battery = view.findViewById(R.id.dd_debug_settings_batery);
        settings = view.findViewById(R.id.dd_debug_settings_settings);
        info = view.findViewById(R.id.dd_debug_settings_info);
        uninstall = view.findViewById(R.id.dd_debug_settings_delete);
        location = view.findViewById(R.id.dd_debug_location_settings);

        developer.setOnClickListener(this);
        battery.setOnClickListener(this);
        settings.setOnClickListener(this);
        info.setOnClickListener(this);
        uninstall.setOnClickListener(this);
        location.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();
        if (v == developer) {
            // open dev settings
            Intent devIntent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            devIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(devIntent, 0);
            if (resolveInfo != null) {
                context.startActivity(devIntent);
            } else {
                Toast.makeText(context, "Developer settings not available on device", Toast.LENGTH_SHORT).show();
            }
        } else if (v == battery) {
            // try to find an app to handle battery settings
            Intent batteryIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
            batteryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(batteryIntent, 0);
            if (resolveInfo != null) {
                context.startActivity(batteryIntent);
            } else {
                Toast.makeText(context, "No app found to handle power usage intent", Toast.LENGTH_SHORT).show();
            }
        } else if (v == settings) {
            // open android settings
            Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
            settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(settingsIntent);
        } else if (v == info) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } else if (v == uninstall) {
            // open dialog to uninstall app
            Uri packageURI = Uri.parse("package:" + context.getPackageName());
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            context.startActivity(uninstallIntent);
        } else if (v == location) {
            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            locationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(locationIntent);
        }
    }
}
