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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import io.palaima.debugdrawer.base.DebugModule;

public class BuildModule implements DebugModule {


    private final Context context;

    private TextView codeLabel;
    private TextView nameLabel;
    private TextView packageLabel;

    public BuildModule(Context context) {
        this.context = context;
    }

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.dd_debug_drawer_module_build, parent, false);
        view.setClickable(false);
        view.setEnabled(false);

        codeLabel = (TextView) view.findViewById(R.id.dd_debug_build_code);
        nameLabel = (TextView) view.findViewById(R.id.dd_debug_build_name);
        packageLabel = (TextView) view.findViewById(R.id.dd_debug_build_package);

        refresh();

        return view;
    }

    @Override
    public void onOpened() {
        refresh();
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

    private void refresh() {
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            codeLabel.setText(String.valueOf(info.versionCode));
            nameLabel.setText(info.versionName);
            packageLabel.setText(info.packageName);
        } catch (PackageManager.NameNotFoundException e) {
        	Log.e("PackageManager.NameNotFoundException", e);
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
