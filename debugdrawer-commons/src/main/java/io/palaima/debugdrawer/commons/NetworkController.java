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

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class NetworkController {

    public enum BluetoothState {
        On, Off, Turning_On, Turning_Off, Unknown
    }

    public interface OnNetworkChangedListener {
        void onChanged(NetworkChangeEvent event);
    }

    private static NetworkController instance;

    private WifiManager         wifiManager;
    private ConnectivityManager connectivityManager;
    private BluetoothAdapter    bluetoothAdapter;

    private NetworkReceiver receiver;

    private transient Context context;

    private OnNetworkChangedListener onNetworkChangedListener;

    public static NetworkController newInstance(Context context) {
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
        this.context = context.getApplicationContext();
        wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void setOnNetworkChangedListener(OnNetworkChangedListener listener) {
        onNetworkChangedListener = listener;
    }

    /**
     * True if WiFi enabled
     */
    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    /**
     * Set WiFi state
     *
     * @param enabled
     */
    public void setWifiEnabled(boolean enabled) {
        wifiManager.setWifiEnabled(enabled);
    }

    /**
     * True if mobile network enabled
     */
    public boolean isMobileNetworkEnabled() {
        final NetworkInfo info = connectivityManager.getNetworkInfo(
            ConnectivityManager.TYPE_MOBILE);
        return (info != null && info.isConnected());
    }

    /**
     * http://stackoverflow.com/questions/11555366/enable-disable-data-connection-in-android-programmatically
     * Try to enabled/disable mobile network state using reflection.
     * Returns true if succeeded
     *
     * @param enabled
     */
    public boolean setMobileNetworkEnabled(boolean enabled) {
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
        	Log.e("ClassNotFoundException", e);
        } catch (InvocationTargetException e) {
        	Log.e("InvocationTargetException", e);
        } catch (NoSuchMethodException e) {
        	Log.e("NoSuchMethodException", e);
        } catch (IllegalAccessException e) {
        	Log.e("IllegalAccessException", e);
        } catch (NoSuchFieldException e) {
        	Log.e("NoSuchFieldException", e);
        }
        return false;
    }

    /**
     * @return True if bluetooth adapter is not null
     */
    public boolean isBluetoothAvailable() {
        return bluetoothAdapter != null;
    }

    /**
     * @return True if bluetooth is enabled
     */
    public boolean isBluetoothEnabled() {
        return isBluetoothAvailable() && bluetoothAdapter.isEnabled();
    }

    /**
     * Try to enable/disabled bluetooth. Returns false if immediate
     * error occurs
     *
     * @param enabled
     */
    public boolean setBluetoothEnabled(boolean enabled) {
        return isBluetoothAvailable() && (enabled ? bluetoothAdapter.enable()
            : bluetoothAdapter.disable());
    }


    /**
     * Unregister network state broadcast receiver
     */
    public void unregisterReceiver() {
        try {
            context.unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
        	Log.e("IllegalArgumentException", e);
        }
    }

    /**
     * Register network state broadcast receiver
     */
    public void registerReceiver() {
        if (receiver == null)
            receiver = new NetworkReceiver(new Listener() {
                @Override
                public void post(NetworkChangeEvent event) {
                    if (onNetworkChangedListener != null) {
                        onNetworkChangedListener.onChanged(event);
                    }
                }
            });
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(receiver, filter);
    }

    /**
     * Receiver that handles wifi, mobile networks and
     * Bluetooth connectivity change intents and sends
     * a NetworkChangeEvent using listener
     * <p/>
     */
    public static class NetworkReceiver extends BroadcastReceiver {

        @Nullable private final Listener listener;

        public NetworkReceiver(@Nullable Listener listener) {
            this.listener = listener;
        }

        @Override public void onReceive(Context context, Intent intent) {
            if (listener != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                BluetoothAdapter bluetoothInfo = BluetoothAdapter.getDefaultAdapter();

                final NetworkChangeEvent networkChangeEvent = new NetworkChangeEvent(
                    (wifiInfo != null) ? wifiInfo.getState() : NetworkInfo.State.UNKNOWN,
                    (mobileInfo != null) ? mobileInfo.getState() : NetworkInfo.State.UNKNOWN,
                    (bluetoothInfo != null) ? getBluetoothState(bluetoothInfo.getState()) : BluetoothState.Unknown);

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
                    return BluetoothState.On;
                case BluetoothAdapter.STATE_OFF:
                    return BluetoothState.Off;
                case BluetoothAdapter.STATE_TURNING_ON:
                    return BluetoothState.Turning_On;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    return BluetoothState.Turning_Off;
            }

            return BluetoothState.Unknown;
        }
    }

    public static class NetworkChangeEvent {
        public final NetworkInfo.State wifiState;
        public final NetworkInfo.State mobileState;
        public final BluetoothState    bluetoothState;

        public NetworkChangeEvent(NetworkInfo.State wifiState, NetworkInfo.State mobileState, BluetoothState bluetoothState) {
            this.wifiState = wifiState;
            this.mobileState = mobileState;
            this.bluetoothState = bluetoothState;
        }
    }

    public interface Listener {
        void post(NetworkChangeEvent event);
    }

}
