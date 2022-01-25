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

package io.palaima.debugdrawer.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

class LocationController implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private transient LocationListener locationListener;

    private static LocationController instance;

    private GoogleApiClient googleApiClient;

    private LocationRequest locationRequest;

    private boolean isStarted;

    private boolean connected;

    static LocationController newInstance(Context context) {
        if (instance == null)
            instance = new LocationController(context);
        return instance;
    }

    private LocationController(Context context) {
        buildGoogleApiClient(context);
    }

    void setLocationListener(LocationListener listener) {
        locationListener = listener;
    }

    @Override
    public void onConnected(Bundle bundle) {
        connected = true;
        if (!isStarted) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        connected = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        connected = false;
    }

    /**
     * Get last known location
     *
     * @return Location
     */
    Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    private synchronized void buildGoogleApiClient(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();
    }

    void setLocationRequest(LocationRequest locationRequest) {
        this.locationRequest = locationRequest;
    }

    void startLocationUpdates() {
        googleApiClient.connect();
        if (connected && locationRequest != null) {
            isStarted = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        }
    }

    void stopLocationUpdates() {
        if (connected && locationRequest != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
            googleApiClient.disconnect();
            connected = false;
            isStarted = false;
        }
    }
}
