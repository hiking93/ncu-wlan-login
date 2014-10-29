package com.sparkslab.ncuwlogin;

import com.sparkslab.ncuwlogin.callbacks.Constant;
import com.sparkslab.ncuwlogin.callbacks.Memory;
import com.sparkslab.ncuwlogin.libs.LoginHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WifiReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();

		if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			WifiManager manager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			NetworkInfo networkInfo = intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			NetworkInfo.State state = networkInfo.getState();

			if (state == NetworkInfo.State.CONNECTED) {
				String ssid = manager.getConnectionInfo().getSSID()
						.replace("\"", "");
				if (ssid.equals("NCUWL")) {
					// connected
					String infoString = "Connected to: " + ssid;
					Toast.makeText(context, infoString, Toast.LENGTH_SHORT)
							.show();
					Log.i("Hiking", infoString);
					String user = Memory.getString(context,
							Constant.MEMORY_KEY_USER, null);
					String password = Memory.getString(context,
							Constant.MEMORY_KEY_PASSWORD, null);
					if (user != null && password != null) {
						LoginHelper.login(context, user, password, null);
					}
				}
			}

			if (state == NetworkInfo.State.DISCONNECTED) {
				if (manager.isWifiEnabled()) {
					// disconnected
					String infoString = "Wi-Fi disconnected.";
					Toast.makeText(context, infoString, Toast.LENGTH_SHORT)
							.show();
					Log.i("Hiking", infoString);
				}
			}
		}
	}
};