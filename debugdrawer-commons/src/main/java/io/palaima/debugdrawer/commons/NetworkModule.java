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
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import io.palaima.debugdrawer.base.DebugModule;

public class NetworkModule implements DebugModule {

    private final Context context;

    private NetworkController networkController;

    private Switch wifi;
    private Switch mobile;
    private Switch bluetooth;

    public NetworkModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.dd_debug_drawer_module_network, parent, false);
        wifi = (Switch) view.findViewById(R.id.dd_debug_network_wifi);
        mobile = (Switch) view.findViewById(R.id.dd_debug_network_mobile);
        // In JellyBean 4.2, mobile network settings are only accessible from system apps
        mobile.setEnabled(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1);
        bluetooth = (Switch) view.findViewById(R.id.dd_debug_network_bluetooth);

        networkController = NetworkController.newInstance(context);

        wifi.setChecked(networkController.isWifiEnabled());
        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                networkController.setWifiEnabled(isChecked);
            }
        });

        mobile.setChecked(networkController.isMobileNetworkEnabled());
        mobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                networkController.setMobileNetworkEnabled(isChecked);
            }
        });

        if (networkController.isBluetoothAvailable()) {
            bluetooth.setChecked(networkController.isBluetoothEnabled());
            bluetooth.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                            networkController.setBluetoothEnabled(isChecked);
                        }
                    });
        } else {
            bluetooth.setEnabled(false);
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
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {
        networkController.setOnNetworkChangedListener(null);
        networkController.unregisterReceiver();
    }

    @Override
    public void onStart() {
        networkController.setOnNetworkChangedListener(onNetworkChangedListener);
        networkController.registerReceiver();
    }

    private NetworkController.OnNetworkChangedListener onNetworkChangedListener = new NetworkController.OnNetworkChangedListener() {
        @Override
        public void onChanged(NetworkController.NetworkChangeEvent event) {
            if (wifi != null) {
                wifi.setChecked(event.wifiState == NetworkInfo.State.CONNECTED
                    || event.wifiState == NetworkInfo.State.CONNECTING);
            }
            if (mobile != null) {
                mobile.setChecked(event.mobileState == NetworkInfo.State.CONNECTED
                    || event.mobileState == NetworkInfo.State.CONNECTING);
            }
            if (bluetooth != null) {
                bluetooth.setChecked(event.bluetoothState == NetworkController.BluetoothState.On
                    || event.bluetoothState == NetworkController.BluetoothState.Turning_On);
            }
        }
    };
}
