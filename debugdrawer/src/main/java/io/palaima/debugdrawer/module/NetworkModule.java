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
import android.net.NetworkInfo;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import io.palaima.debugdrawer.R;
import io.palaima.debugdrawer.controller.NetworkController;

public class NetworkModule implements DrawerModule {

    private final NetworkController mNetworkController;

    private Switch mWifi;
    private Switch mMobile;
    private Switch mBluetooth;

    public NetworkModule(Context context) {
        mNetworkController = NetworkController.newInstance(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.debug_drawer_module_network, parent, false);
        mWifi = (Switch) view.findViewById(R.id.debug_network_wifi);
        mMobile = (Switch) view.findViewById(R.id.debug_network_mobile);
        // In JellyBean 4.2, mobile network settings are only accessible from system apps
        mMobile.setEnabled(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1);
        mBluetooth = (Switch) view.findViewById(R.id.debug_network_bluetooth);


        mNetworkController.setOnNetworkChangedListener(
                new NetworkController.OnNetworkChangedListener() {
                    @Override
                    public void onChanged(NetworkController.NetworkChangeEvent event) {
                        mWifi.setChecked(event.wifiState == NetworkInfo.State.CONNECTED || event.wifiState == NetworkInfo.State.CONNECTING);
                        mMobile.setChecked(event.mobileState == NetworkInfo.State.CONNECTED || event.mobileState == NetworkInfo.State.CONNECTING);
                        mBluetooth.setChecked(event.bluetoothState == NetworkController.BluetoothState.On || event.bluetoothState == NetworkController.BluetoothState.Turning_On);
                    }
                });

        mWifi.setChecked(mNetworkController.isWifiEnabled());
        mWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                mNetworkController.setWifiEnabled(isChecked);
            }
        });

        mMobile.setChecked(mNetworkController.isMobileNetworkEnabled());
        mMobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                mNetworkController.setMobileNetworkEnabled(isChecked);
            }
        });

        if (mNetworkController.isBluetoothAvailable()) {
            mBluetooth.setChecked(mNetworkController.isBluetoothEnabled());
            mBluetooth.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                            mNetworkController.setBluetoothEnabled(isChecked);
                        }
                    });
        } else {
            mBluetooth.setEnabled(false);
        }


        return view;
    }

    @Override
    public void onOpened() {

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onStop() {
        mNetworkController.unregisterReceiver();
    }

    @Override
    public void onStart() {
        mNetworkController.registerReceiver();
    }
}
