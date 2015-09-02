package com.example.currentlocation.googlel.ocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.currentlocation.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

/**
 * Created by Serhiy Petrosyuk on 12.08.15.
 */
public class GoogleApi implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleApiClient mGoogleApiClient;
    private Geocoder mGeocoder;
    private LocationCallback<Bundle> mOnConnectionCallback;
    private LocationCallback<Address> mGetAddressCallback;
    private final long INTERVAL = 100000;

    @Override
    public void onConnected(Bundle bundle) {
        if (mOnConnectionCallback != null)
            mOnConnectionCallback.onSuccess(bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        new LocationGeocoder(location, mGetAddressCallback).execute();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mOnConnectionCallback != null)
            mOnConnectionCallback.onError(connectionResult.toString());
    }

    public void connect(LocationCallback<Bundle> callback) {
        mOnConnectionCallback = callback;
        mGoogleApiClient.connect();
    }

    public void disconnect() {
        mOnConnectionCallback = null;
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    public void buildGoogleApiClient(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGeocoder = new Geocoder(context);
    }

    public void detectCurrentLocation(LocationCallback<Address> callback) {
        mGetAddressCallback = callback;
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationGeocoder locationGeocoder = new LocationGeocoder(location, mGetAddressCallback);
        locationGeocoder.execute();
    }

    public void detectCurrentLocationWithUpdating(LocationCallback<Address> callback) {
        mGetAddressCallback = callback;
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, GoogleApi.this);
    }

    public interface LocationCallback<T> {
        void onSuccess(T data);
        void onError(String errorMessage);
    }

    public class LocationGeocoder extends AsyncTask<Void, Void, Address> {
        private Location mLocation;
        LocationCallback<Address> mCallback;

        public LocationGeocoder(Location mLocation, LocationCallback<Address> mCallback) {
            this.mLocation = mLocation;
            this.mCallback = mCallback;
        }

        @Override
        protected Address doInBackground(Void... params) {
            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();
            Address address = null;
            try {
                List<Address> addressList = mGeocoder.getFromLocation(latitude, longitude, 1);
                if (addressList.size() > 0)
                    address = addressList.get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return address;
        }

        @Override
        protected void onPostExecute(Address address) {
            if (address != null) {
                mCallback.onSuccess(address);
            } else {
                mCallback.onError("Can't detect location!");
            }
        }
    }

    public void buildAlertDialogNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.alert_message_gps_disabled))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.dialog_btn_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(context.getString(R.string.dialog_btn_negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}
