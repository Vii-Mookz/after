package com.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by IntelliJ IDEA.
 * User: Jim
 * Date: 1/9/13
 * Time: 4:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class NetworkProviderStatusReceiver extends BroadcastReceiver {
    final String _logTag = "Monitor Location";
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        Log.d(_logTag, "Monitor Location Broadcast Receiver Action:" + action);

        if(action.equalsIgnoreCase(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
            boolean state = extras.getBoolean("state");
            Log.d(_logTag, String.format("Monitor Location Airplane Mode changed to " +
                    (state ? "ON" : "OFF")));
        }
        else if (action.equalsIgnoreCase(ConnectivityManager.CONNECTIVITY_ACTION)){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            Log.d(_logTag, String.format("Monitor Location WiFi Radio Available: " +
                    (wifiInfo.isAvailable() ? "YES" : "NO")));

        }

    }

    public void start(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, filter);
    }

    public void stop(Context context) {
        context.unregisterReceiver(this);
    }
}
