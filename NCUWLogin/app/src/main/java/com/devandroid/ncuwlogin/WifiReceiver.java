package com.devandroid.ncuwlogin;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.devandroid.ncuwlogin.callbacks.Constant;
import com.devandroid.ncuwlogin.libs.LoginHelper;

public class WifiReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();

		if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			NetworkInfo.State state = networkInfo.getState();

			if (state == NetworkInfo.State.CONNECTED) {
				String ssid = manager.getConnectionInfo().getSSID().replace("\"", "");
				LoginHelper.HotspotType hotspotType = LoginHelper.getHotspotType(ssid);

				if (hotspotType != LoginHelper.HotspotType.UNKNOWN) {
					String infoString =
							String.format(context.getString(R.string.connected_to_ssid), ssid);
					Toast.makeText(context, infoString, Toast.LENGTH_SHORT).show();
				}

				switch (hotspotType) {
					case NCUWLAN:
						NCUWLFragment.login(context, null);
						break;
					case NCUCSIE:
						NCUCSIEFragment.login(context, null);
						break;
				}
			}

			if (state == NetworkInfo.State.DISCONNECTED) {
				if (manager.isWifiEnabled()) {
					// disconnected
					String infoString = "Wi-Fi disconnected.";
					Log.i(Constant.TAG, infoString);
					NotificationManager notificationManager = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					notificationManager.cancel(Constant.NOTIFICATION_LOGIN_ID);
				}
			}
		}
	}
};