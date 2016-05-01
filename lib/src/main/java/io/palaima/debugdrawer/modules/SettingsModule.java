/*
 * Copyright (C) 2014 LemonLabs
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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import io.palaima.debugdrawer.DebugWidgets;
import io.palaima.debugdrawer.DebugModule;
import io.palaima.debugdrawer.R;

public class SettingsModule implements DebugModule {

    private final Activity activity;

    public SettingsModule(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public String getName() {
        return "Settings";
    }

    @Override
    public DebugWidgets createWidgets(DebugWidgets.DebugWidgetsBuilder builder) {
        return builder
                .addIconButton("Developer Tools", R.drawable.tool, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open dev settings
                        Intent devIntent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ResolveInfo resolveInfo = activity.getPackageManager().resolveActivity(devIntent, 0);
                        if (resolveInfo != null) {
                            activity.startActivity(devIntent);
                        } else {
                            Toast.makeText(activity, "Developer settings not available on device", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addIconButton("Battery", R.drawable.energy, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // try to find an app to handle battery settings
                        Intent batteryIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ResolveInfo resolveInfo = activity.getPackageManager().resolveActivity(batteryIntent, 0);
                        if (resolveInfo != null) {
                            activity.startActivity(batteryIntent);
                        } else {
                            Toast.makeText(activity, "No app found to handle power usage intent", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addIconButton("Location", R.drawable.location, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(locationIntent);
                    }
                })
                .addIconButton("Settings", R.drawable.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open android settings
                        Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(settingsIntent);
                    }
                })
                .addIconButton("App Info", R.drawable.info, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .setData(Uri.parse("package:" + activity.getPackageName()));
                        activity.startActivity(intent);
                    }
                })
                .addIconButton("Uninstall", R.drawable.delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open dialog to uninstall app
                        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
                        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                        activity.startActivity(uninstallIntent);
                    }
                })
                .build();
    }
}
