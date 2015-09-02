package com.example.currentlocation.presenter;

import android.content.Context;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.currentlocation.googlel.ocation.GoogleApi;
import com.example.currentlocation.view.LocationView;

/**
 * Created by Serhiy Petrosyuk on 12.08.15.
 */
public class LocationPresenterImpl implements LocationPresenter {
    private final String LOG_TAG = "Location presenter";
    private Context mContext;
    private GoogleApi mGoogleApi;
    private LocationView mView;
    private GoogleApi.LocationCallback<Bundle> mConnectionLocation;
    private GoogleApi.LocationCallback<Address> mDetectLocationCallback;

    public LocationPresenterImpl(Context mContext) {
        this.mContext = mContext;
        mGoogleApi = new GoogleApi();
    }

    @Override
    public void setView(LocationView view) {
        mView = view;
        mGoogleApi.buildGoogleApiClient(mContext);
    }

    @Override
    public void onStart() {
        mConnectionLocation = new GoogleApi.LocationCallback<Bundle>() {
            @Override
            public void onSuccess(Bundle data) {
                Log.i(LOG_TAG, "Google Api has been Connected");
                detectCurrentLocation();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(mContext, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Error: " + errorMessage);
            }
        };

        mDetectLocationCallback = new GoogleApi.LocationCallback<Address>() {
            @Override
            public void onSuccess(Address address) {
                mView.setLatitude(Double.toString(address.getLatitude()));
                mView.setLongitude(Double.toString(address.getLongitude()));
                mView.setCity(address.getLocality());
                mView.showProgressBar(false);
            }

            @Override
            public void onError(String errorMessage) {
                mView.showProgressBar(false);
                Toast.makeText(mContext, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Error: " + errorMessage);
            }
        };

        mGoogleApi.connect(mConnectionLocation);
    }

    @Override
    public void onStop() {
        mConnectionLocation = null;
        mDetectLocationCallback = null;
        mGoogleApi.disconnect();
    }

    @Override
    public void detectCurrentLocation() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mView.showProgressBar(true);
            mGoogleApi.detectCurrentLocationWithUpdating(mDetectLocationCallback);
        } else {
            mGoogleApi.buildAlertDialogNoGps(mContext);
        }
    }
}
