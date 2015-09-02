package com.example.currentlocation.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.currentlocation.R;
import com.example.currentlocation.presenter.LocationPresenter;
import com.example.currentlocation.presenter.LocationPresenterImpl;

public class MainActivity extends AppCompatActivity implements LocationView {
    private LocationPresenter mPresenter;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private TextView mCityTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mLatitudeTextView = (TextView) findViewById(R.id.tv_latitude);
        mLongitudeTextView = (TextView) findViewById(R.id.tv_longitude);
        mCityTextView = (TextView) findViewById(R.id.tv_city);

        mPresenter = new LocationPresenterImpl(this);
        mPresenter.setView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mPresenter.detectCurrentLocation();
        return id == R.id.action_update || super.onOptionsItemSelected(item);
    }

    @Override
    public void setLatitude(String latitude) {
        mLatitudeTextView.setText(latitude);
    }

    @Override
    public void setLongitude(String longitude) {
        mLongitudeTextView.setText(longitude);
    }

    @Override
    public void setCity(String city) {
        mCityTextView.setText(city);
    }

    @Override
    public void showProgressBar(boolean isShow) {
        mProgressBar.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }
}
