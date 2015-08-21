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

package io.palaima.debugdrawer.location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.palaima.debugdrawer.module.DrawerModule;

public class LocationModule implements DrawerModule {

    private transient final Context mContext;

    @Nullable
    private LocationController mLocationController;

    private TextView mLatitude;

    private TextView mLongitude;

    private TextView mAccuracy;

    private TextView mTime;

    private TextView mProvider;

    private Location mLocation;

    private boolean mOpened;

    public LocationModule(Context context) {
        this(context, true);
    }

    /**
     * @param context context
     * @param locationRequestsAvailable defines if location should be updated every 10 seconds
     */
    public LocationModule(Context context, boolean locationRequestsAvailable) {
        this(context, locationRequestsAvailable ? new LocationRequest()
                .setInterval(TimeUnit.SECONDS.toMillis(10))
                .setFastestInterval(TimeUnit.SECONDS.toMillis(5))
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) : null);
    }

    public LocationModule(Context context, long interval, long fastestInterval) {
        this(context, new LocationRequest()
                .setInterval(interval)
                .setFastestInterval(fastestInterval)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
    }

    public LocationModule(Context context, LocationRequest locationRequest) {
        mContext = context.getApplicationContext();
        boolean available = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(mContext) == ConnectionResult.SUCCESS;
        if (available) {
            mLocationController = LocationController.newInstance(mContext);
            if (locationRequest != null) {
                mLocationController.setLocationRequest(locationRequest);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        if (mLocationController != null) {
            View view = inflater.inflate(R.layout.debug_drawer_module_location, parent, false);
            mLatitude = (TextView) view.findViewById(R.id.debug_location_latitude);
            mLongitude = (TextView) view.findViewById(R.id.debug_location_longitude);
            mAccuracy = (TextView) view.findViewById(R.id.debug_location_accuracy);
            mTime = (TextView) view.findViewById(R.id.debug_location_time);
            mProvider = (TextView) view.findViewById(R.id.debug_location_provider);
            view.findViewById(R.id.debug_location_map).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openMaps(mContext);
                        }
                    });
            mLocationController.setLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (mOpened) {
                        updateLocation(location);
                    }
                }
            });
            updateLastLocation();
            return view;
        } else {
            TextView errorText = new TextView(mContext);
            errorText.setTextAppearance(mContext, R.style.Widget_DebugDrawer_Header);
            errorText.setText(R.string.debug_drawer_location_google_play_unavailable);
            return errorText;
        }
    }

    @Override
    public void onOpened() {
        updateLastLocation();
        mOpened = true;
    }

    @Override
    public void onClosed() {
        mOpened = false;
    }

    private void updateLastLocation() {
        if (mLocationController != null) {
            Location lastLocation = mLocationController.getLastLocation();
            updateLocation(lastLocation);
        }
    }

    private void updateLocation(Location location) {
        if (location != null) {
            mLocation = location;
            mLatitude.setText(String.valueOf(location.getLatitude()));
            mLongitude.setText(String.valueOf(location.getLongitude()));
            mAccuracy.setText(String.valueOf(location.getAccuracy()) + "m");

            Date date = new Date(location.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mTime.setText(sdf.format(date));
            mProvider.setText(location.getProvider());
        } else {
            mLatitude.setText(R.string.debug_drawer_location_empty);
            mLongitude.setText(R.string.debug_drawer_location_empty);
            mAccuracy.setText(R.string.debug_drawer_location_empty);
            mTime.setText(R.string.debug_drawer_location_empty);
            mProvider.setText(R.string.debug_drawer_location_no_provider);
        }
    }

    @Override
    public void onStart() {
        if (mLocationController != null) {
            mLocationController.startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        if (mLocationController != null) {
            mLocationController.stopLocationUpdates();
        }
    }

    private void openMaps(Context context) {
        try {
            if (mLocation != null) {
                String uri = "geo:" + mLocation.getLatitude() + "," + mLocation.getLongitude();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, R.string.debug_drawer_location_not_found, Toast.LENGTH_SHORT).show();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.debug_drawer_location_map_not_found, Toast.LENGTH_SHORT).show();
        }
    }
}
