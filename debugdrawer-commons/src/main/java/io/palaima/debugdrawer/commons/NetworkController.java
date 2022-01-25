/*
 * Copyright (C) 2014 LemonLabs
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

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

final class NetworkController {

    enum BluetoothState {
        ON, OFF, TURNING_ON, TURNING_OFF, UNKNOWN
    }

    interface OnNetworkChangedListener {
        void onChanged(NetworkChangeEvent event);
    }

    private static NetworkController instance;

    private final WeakReference<Context> contextRef;

    private NetworkReceiver receiver;

    private OnNetworkChangedListener onNetworkChangedListener;

    static NetworkController newInstance(Context context) {
        if (instance == null)
            instance = new NetworkController(context);
        return instance;
    }

    /**
     * Controller responsible for switching states related to networks.
     * E.g. wifi, mobile networks
     *
     * @param context
     */
    private NetworkController(Context context) {
        contextRef = new WeakReference<>(context.getApplicationContext());
    }

    void setOnNetworkChangedListener(OnNetworkChangedListener listener) {
        onNetworkChangedListener = listener;
    }

    /**
     * Unregister network state broadcast receiver
     */
    void unregisterReceiver() {
        try {
            final Context context = contextRef.get();
            if (context != null) {
                context.unregisterReceiver(receiver);
            }
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Register network state broadcast receiver
     */
    void registerReceiver() {
        if (receiver == null) {
            receiver = new NetworkReceiver(new Listener() {
                @Override
                public void post(NetworkChangeEvent event) {
                    if (onNetworkChangedListener != null) {
                        onNetworkChangedListener.onChanged(event);
                    }
                }
            });
        }
        final Context context = contextRef.get();
        if (context != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(receiver, filter);
        }
    }

    private static boolean hasBluetoothPermission(Context context) {
        return context.checkCallingOrSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Receiver that handles wifi, mobile networks and
     * Bluetooth connectivity change intents and sends
     * a NetworkChangeEvent using listener
     * <p/>
     */
    static class NetworkReceiver extends BroadcastReceiver {

        @Nullable
        private final Listener listener;

        public NetworkReceiver(@Nullable Listener listener) {
            this.listener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (listener != null) {
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                final NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                final NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                final BluetoothAdapter bluetoothInfo = BluetoothAdapter.getDefaultAdapter();

                final NetworkChangeEvent networkChangeEvent = new NetworkChangeEvent(
                    (wifiInfo != null) ? wifiInfo.getState() : NetworkInfo.State.UNKNOWN,
                    (mobileInfo != null) ? mobileInfo.getState() : NetworkInfo.State.UNKNOWN,
                    (bluetoothInfo != null && hasBluetoothPermission(context)) ? getBluetoothState(bluetoothInfo.getState()) : BluetoothState.UNKNOWN);

                listener.post(networkChangeEvent);
            }

        }

        /**
         * Converts Bluetooth state representation to an Enum
         *
         * @param state
         */
        private BluetoothState getBluetoothState(int state) {
            switch (state) {
                case BluetoothAdapter.STATE_ON:
                    return BluetoothState.ON;
                case BluetoothAdapter.STATE_OFF:
                    return BluetoothState.OFF;
                case BluetoothAdapter.STATE_TURNING_ON:
                    return BluetoothState.TURNING_ON;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    return BluetoothState.TURNING_OFF;
            }

            return BluetoothState.UNKNOWN;
        }
    }

    static class NetworkChangeEvent {
        final NetworkInfo.State wifiState;
        final NetworkInfo.State mobileState;
        final BluetoothState bluetoothState;

        NetworkChangeEvent(NetworkInfo.State wifiState, NetworkInfo.State mobileState, BluetoothState bluetoothState) {
            this.wifiState = wifiState;
            this.mobileState = mobileState;
            this.bluetoothState = bluetoothState;
        }
    }

    interface Listener {
        void post(NetworkChangeEvent event);
    }
}
