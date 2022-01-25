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

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.palaima.debugdrawer.base.DebugModule;

public class LocationModule implements DebugModule {

    private static final boolean HAS_LOCATION;

    static {
        boolean hasDependency;

        try {
            Class.forName("com.google.android.gms.location.LocationRequest");
            hasDependency = true;
        } catch (ClassNotFoundException e) {
            hasDependency = false;
        }

        HAS_LOCATION = hasDependency;
    }


    private final LocationRequest locationRequest;

    private WeakReference<Context> contextRef;

    private boolean hasPermission;

    @Nullable
    private LocationController locationController;

    private TextView latitude;

    private TextView longitude;

    private TextView accuracy;

    private TextView time;

    private TextView provider;

    private Location location;

    private boolean opened;

    public LocationModule() {
        this(true);
    }

    /**
     * @param locationRequestsAvailable defines if location should be updated every 10 seconds
     */
    public LocationModule(boolean locationRequestsAvailable) {
        this(HAS_LOCATION && locationRequestsAvailable ? new LocationRequest()
            .setInterval(TimeUnit.SECONDS.toMillis(10))
            .setFastestInterval(TimeUnit.SECONDS.toMillis(5))
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) : null);
    }

    public LocationModule(long interval, long fastestInterval) {
        this(HAS_LOCATION ? new LocationRequest()
            .setInterval(interval)
            .setFastestInterval(fastestInterval)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) : null);
    }

    public LocationModule(LocationRequest locationRequest) {
        if (!HAS_LOCATION) {
            throw new RuntimeException("Google Play location dependency is not found");
        }
        this.locationRequest = locationRequest;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (contextRef == null) {
            contextRef = new WeakReference<>(parent.getContext());
        }

        final Context context = contextRef.get();

        checkPermission();
        final GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        final boolean available = api.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
        if (available && hasPermission) {
            locationController = LocationController.newInstance(context);
            if (locationRequest != null) {
                locationController.setLocationRequest(locationRequest);
            }
        }

        if (locationController != null && hasPermission) {
            final View view = inflater.inflate(R.layout.dd_debug_drawer_module_location, parent, false);
            latitude = (TextView) view.findViewById(R.id.dd_debug_location_latitude);
            longitude = (TextView) view.findViewById(R.id.dd_debug_location_longitude);
            accuracy = (TextView) view.findViewById(R.id.dd_debug_location_accuracy);
            time = (TextView) view.findViewById(R.id.dd_debug_location_time);
            provider = (TextView) view.findViewById(R.id.dd_debug_location_provider);
            view.findViewById(R.id.dd_debug_location_map).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMaps(context);
                    }
                });
            locationController.setLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (opened) {
                        updateLocation(location);
                    }
                }
            });
            updateLastLocation();
            return view;
        } else if (!hasPermission) {
            final TextView errorText = new TextView(context);
            errorText.setTextAppearance(context, R.style.Widget_DebugDrawer_Base_Header);
            errorText.setText(R.string.dd_debug_drawer_location_no_permission);
            return errorText;
        } else {
            final TextView errorText = new TextView(context);
            errorText.setTextAppearance(context, R.style.Widget_DebugDrawer_Base_Header);
            errorText.setText(R.string.dd_debug_drawer_location_google_play_unavailable);
            return errorText;
        }
    }

    @Override
    public void onOpened() {
        updateLastLocation();
        opened = true;
    }

    @Override
    public void onClosed() {
        opened = false;
    }

    @Override
    public void onResume() {
        updateLastLocation();
        opened = true;
    }

    @Override
    public void onPause() {
        opened = false;
    }

    private void updateLastLocation() {
        if (locationController != null) {
            final Location lastLocation = locationController.getLastLocation();
            updateLocation(lastLocation);
        }
    }

    private void updateLocation(Location location) {
        if (location != null) {
            this.location = location;
            latitude.setText(String.valueOf(location.getLatitude()));
            longitude.setText(String.valueOf(location.getLongitude()));
            accuracy.setText(String.valueOf(location.getAccuracy()) + "m");

            final Date date = new Date(location.getTime());
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time.setText(sdf.format(date));
            provider.setText(location.getProvider());
        } else {
            latitude.setText(R.string.dd_debug_drawer_location_empty);
            longitude.setText(R.string.dd_debug_drawer_location_empty);
            accuracy.setText(R.string.dd_debug_drawer_location_empty);
            time.setText(R.string.dd_debug_drawer_location_empty);
            provider.setText(R.string.dd_debug_drawer_location_no_provider);
        }
    }

    @Override
    public void onStart() {
        if (locationController != null) {
            checkPermission();
            locationController.startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        if (locationController != null) {
            locationController.stopLocationUpdates();
        }
    }

    private void openMaps(Context context) {
        try {
            if (location != null) {
                final String uri = "geo:" + location.getLatitude() + "," + location.getLongitude();
                final Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, R.string.dd_debug_drawer_location_not_found, Toast.LENGTH_SHORT).show();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.dd_debug_drawer_location_map_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermission() {
        final Context context = contextRef.get();
        if (context != null) {
            hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        }
    }
}
