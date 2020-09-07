package com.devandroid.ncuwlogin.libs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Utils {

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null && networkInfo.isConnected()) {
            final WifiManager wifiManager =
                    (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && connectionInfo.getSSID() != null &&
                    !connectionInfo.getSSID().isEmpty()) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid == null ? ssid : ssid.replace("\"", "");
    }
}
