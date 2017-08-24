package com.example;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Jim
 * Date: 1/1/13
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyLocationListener implements LocationListener{
    final String _logTag = "Monitor Location";

    public void onLocationChanged(Location location) {
        String provider = location.getProvider();
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        float accuracy = location.getAccuracy();
        long time = location.getTime();

        String logMessage = LogHelper.formatLocationInfo(provider, lat, lng, accuracy, time);
        Log.d(_logTag, "Monitor Location:" + logMessage);
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
        String statusMessage = LogHelper.translateStatus(i);
        Log.d(_logTag, "Monitor Location - Status:" + statusMessage);
        Set<String> keys = bundle.keySet();
        for(String key:keys){
            Log.d(_logTag, "Monitor Location - Bundle Key:" + key);
        }
    }

    public void onProviderEnabled(String s) {
        Log.d(_logTag, "Monitor Location - Provider ENABLED by USER: " + s);
    }

    public void onProviderDisabled(String s) {
        Log.d(_logTag, "Monitor Location - Provider DISABLED by USER: " + s);
    }
}
