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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import io.palaima.debugdrawer.base.DebugModuleAdapter;

public class BuildModule extends DebugModuleAdapter {

    private WeakReference<Context> context;

    private TextView codeLabel;
    private TextView nameLabel;
    private TextView packageLabel;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {

        if (context == null) {
            context = new WeakReference<>(parent.getContext());
        }

        final View view = inflater.inflate(R.layout.dd_debug_drawer_module_build, parent, false);
        view.setClickable(false);
        view.setEnabled(false);

        codeLabel = view.findViewById(R.id.dd_debug_build_code);
        nameLabel = view.findViewById(R.id.dd_debug_build_name);
        packageLabel = view.findViewById(R.id.dd_debug_build_package);

        refresh();

        return view;
    }

    @Override
    public void onOpened() {
        refresh();
    }

    private void refresh() {
        try {
            final Context context = this.context.get();
            if (context != null) {
                final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                codeLabel.setText(String.valueOf(info.versionCode));
                nameLabel.setText(info.versionName);
                packageLabel.setText(info.packageName);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }
}
