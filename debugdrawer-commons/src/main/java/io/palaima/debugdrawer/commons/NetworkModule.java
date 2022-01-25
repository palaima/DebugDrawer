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

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.palaima.debugdrawer.base.DebugModuleAdapter;

public class NetworkModule extends DebugModuleAdapter {

    private NetworkController networkController;

    private Switch wifi;
    private Switch mobile;
    private Switch bluetooth;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final Context context = parent.getContext().getApplicationContext();
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final View view = inflater.inflate(R.layout.dd_debug_drawer_module_network, parent, false);

        wifi = view.findViewById(R.id.dd_debug_network_wifi);
        mobile = view.findViewById(R.id.dd_debug_network_mobile);
        // In JellyBean 4.2, mobile network settings are only accessible from system apps
        final boolean mobileToggleAvailable = Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1;
        mobile.setVisibility(mobileToggleAvailable ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.dd_debug_network_mobile_label).setVisibility(mobileToggleAvailable ? View.VISIBLE : View.GONE);
        bluetooth = view.findViewById(R.id.dd_debug_network_bluetooth);

        networkController = NetworkController.newInstance(context);

        wifi.setChecked(wifiManager.isWifiEnabled());
        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                wifiManager.setWifiEnabled(isChecked);
            }
        });

        mobile.setChecked(isMobileNetworkEnabled(connectivityManager));
        mobile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                setMobileNetworkEnabled(connectivityManager, isChecked);
            }
        });

        if (bluetoothAdapter != null) {
            bluetooth.setChecked(hasBluetoothPermission(context) && bluetoothAdapter.isEnabled());
            bluetooth.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                        if (isChecked) {
                            bluetoothAdapter.enable();
                        } else {
                            bluetoothAdapter.disable();
                        }
                    }
                });
        } else {
            bluetooth.setEnabled(false);
        }

        return view;
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

    /**
     * True if mobile network enabled
     */
    private boolean isMobileNetworkEnabled(ConnectivityManager connectivityManager) {
        final NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (info != null && info.isConnected());
    }

    /**
     * http://stackoverflow.com/questions/11555366/enable-disable-data-connection-in-android-programmatically
     * Try to enabled/disable mobile network state using reflection.
     * Returns true if succeeded
     *
     * @param enabled
     */
    private boolean setMobileNetworkEnabled(ConnectivityManager connectivityManager, boolean enabled) {
        try {
            final Class conmanClass = Class.forName(connectivityManager.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(connectivityManager);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
            return true;
        } catch (ClassNotFoundException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (NoSuchFieldException e) {
        }
        return false;
    }

    private boolean hasBluetoothPermission(Context context) {
        return context.checkCallingOrSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED;
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
                bluetooth.setChecked(event.bluetoothState == NetworkController.BluetoothState.ON
                    || event.bluetoothState == NetworkController.BluetoothState.TURNING_ON);
            }
        }
    };
}
