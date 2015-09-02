package com.example.currentlocation.presenter;

import com.example.currentlocation.view.LocationView;

/**
 * Created by Serhiy Petrosyuk on 12.08.15.
 */
public interface LocationPresenter {
    void setView(LocationView view);
    void onStart();
    void onStop();
    void detectCurrentLocation();
}
