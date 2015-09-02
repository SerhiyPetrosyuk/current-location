package com.example.currentlocation.view;

/**
 * Created by Serhiy Petrosyuk on 12.08.15.
 */
public interface LocationView {
    void setLatitude(String latitude);
    void setLongitude(String longitude);
    void setCity(String city);
    void showProgressBar(boolean isShow);
}
