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

package io.palaima.debugdrawer.modules;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.CompoundButton;

import io.palaima.debugdrawer.DebugModule;
import io.palaima.debugdrawer.DebugWidgets;
import io.palaima.debugdrawer.util.NetworkController;


public class NetworkModule implements DebugModule {

    private NetworkController networkController;

    private Context context;

    public NetworkModule(Context context) {
        this.context = context;
        networkController = NetworkController.newInstance(context);
    }

    @NonNull
    @Override
    public String getName() {
        return "Network";
    }

    @Override
    public DebugWidgets createWidgets(DebugWidgets.DebugWidgetsBuilder builder) {
        builder.addSwitch("Wifi", networkController.isWifiEnabled(),
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        networkController.setWifiEnabled(isChecked);
                    }
                });

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // In JellyBean 4.2, mobile network settings are only accessible from system apps
            builder.addText("Mobile", networkController.isMobileNetworkEnabled());
        } else {
            builder.addSwitch("Mobile", networkController.isMobileNetworkEnabled(),
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            networkController.setMobileNetworkEnabled(isChecked);
                        }
                    });
        }

        if (networkController.isBluetoothAvailable()) {
            builder.addSwitch("Bluetooth", networkController.isBluetoothEnabled(),
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            networkController.setBluetoothEnabled(isChecked);
                        }
                    });
        }
        return builder.build();
    }

}
