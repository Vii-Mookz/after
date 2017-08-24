package com.example;

import android.app.Activity;
import android.content.Intent;
import android.location.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

public class MyActivity extends Activity
{
    LocationListener _networkLocationListener;
    LocationListener _passiveLocationListener;

    NetworkProviderStatusReceiver _statusReceiver;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater() ;
        inflater.inflate(R.menu.main_menu, menu) ;
        return true;
    }

    public void onAccurateProvider(MenuItem item) {
        Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(true);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> matchingProviderNames = lm.getProviders(criteria, false);
        for(String providerName:matchingProviderNames) {
            LocationProvider provider = lm.getProvider(providerName);
            String logMessage = LogHelper.formationLocationProvider(this, provider);
            Log.d(LogHelper.LOGTAG, "Monitor Location Provider:" + logMessage);
        }
    }

    public void onLowPowerProvider(MenuItem item) {
        Criteria criteria = new Criteria();

        criteria.setPowerRequirement(Criteria.POWER_LOW);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> matchingProviderNames = lm.getProviders(criteria, false);
        for(String providerName:matchingProviderNames) {
            LocationProvider provider = lm.getProvider(providerName);
            String logMessage = LogHelper.formationLocationProvider(this, provider);
            Log.d(LogHelper.LOGTAG, "Monitor Location Provider:" + logMessage);
        }
    }

    public void onStartNetworkListener(MenuItem item) {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(confirmNetworkProviderAvailable(lm)) {
            _statusReceiver = new NetworkProviderStatusReceiver();
            _statusReceiver.start(this);

            _networkLocationListener = new MyLocationListener();
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, _networkLocationListener);
        }
    }

    public void onStartPassiveListener(MenuItem item) {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        _passiveLocationListener = new MyLocationListener();
        lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, _passiveLocationListener);
    }

    public void onExit(MenuItem item) {
        stopLocationListener();
        finish();
    }

    private void stopLocationListener() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (_networkLocationListener != null) {
            lm.removeUpdates(_networkLocationListener);
            _networkLocationListener = null;
        }

        if (_statusReceiver != null) {
            _statusReceiver.stop(this);
            _statusReceiver = null;
        }

    }

    boolean confirmNetworkProviderAvailable(LocationManager lm) {
        return confirmNetworkProviderEnabled(lm) &&
                confirmAirplaneModeOff() &&
                confirmWiFiAvailable();
    }

    boolean confirmNetworkProviderEnabled(LocationManager lm) {

        boolean isAvailable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!isAvailable) {
            AlertUserDialog dialog = new AlertUserDialog("Please Enable Location Services",
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            dialog.show(getFragmentManager(), null);
        }

        return isAvailable;
    }

    boolean confirmAirplaneModeOff() {
        boolean isOff =
                Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 0;

        if(!isOff) {
            AlertUserDialog dialog = new AlertUserDialog("Please Disable Airplane Mode",
                    Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            dialog.show(getFragmentManager(), null);
        }

        return isOff;
    }

    boolean confirmWiFiAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isAvailable = wifiInfo.isAvailable();

        if(!isAvailable) {
            AlertUserDialog dialog = new AlertUserDialog("Please Turn On Wi-Fi",
                    Settings.ACTION_WIFI_SETTINGS);
            dialog.show(getFragmentManager(), null);
        }

        return isAvailable;
    }

}
