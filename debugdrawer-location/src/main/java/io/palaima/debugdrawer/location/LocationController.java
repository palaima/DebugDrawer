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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

public class LocationController implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private transient LocationListener mLocationListener;

    private static LocationController instance;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private boolean mIsStarted;

    private boolean mConnected;

    public static LocationController newInstance(Context context) {
        if (instance == null)
            instance = new LocationController(context);
        return instance;
    }

    private LocationController(Context context) {
        buildGoogleApiClient(context);
    }

    public void setLocationListener(LocationListener listener) {
        mLocationListener = listener;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mConnected = true;
        if (!mIsStarted) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mConnected = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        mConnected = false;
    }

    /**
     * Get last known location
     *
     * @return Location
     */
    public Location getLastLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    protected synchronized void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        mLocationRequest = locationRequest;
    }

    public void startLocationUpdates() {
        mGoogleApiClient.connect();
        if (mConnected && mLocationRequest != null) {
            mIsStarted = true;
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, mLocationListener);
        }
    }

    public void stopLocationUpdates() {
        if (mConnected && mLocationRequest != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, mLocationListener);
            mGoogleApiClient.disconnect();
            mConnected = false;
            mIsStarted = false;
        }
    }
}
