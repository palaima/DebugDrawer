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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.palaima.debugdrawer.R;

public class BuildModule implements DrawerModule {


    private final Context mContext;

    private View mRootView;
    private TextView mCode;
    private TextView mName;
    private TextView mPackage;

    public BuildModule(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.debug_drawer_module_build, parent, false);
        mRootView = view;
        mCode = (TextView) view.findViewById(R.id.debug_build_code);
        mName = (TextView) view.findViewById(R.id.debug_build_name);
        mPackage = (TextView) view.findViewById(R.id.debug_build_package);

        mRootView.setClickable(false);
        mRootView.setEnabled(false);

        refresh();

        return view;
    }

    @Override
    public void onRefreshView() {
        refresh();
    }

    private void refresh() {
        try {
            final PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            mCode.setText(String.valueOf(info.versionCode));
            mName.setText(info.versionName);
            mPackage.setText(info.packageName);
        } catch (PackageManager.NameNotFoundException e) {}
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
