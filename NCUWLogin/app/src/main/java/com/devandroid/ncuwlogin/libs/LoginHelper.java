package com.devandroid.ncuwlogin.libs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.devandroid.ncuwlogin.MainActivity;
import com.devandroid.ncuwlogin.R;
import com.devandroid.ncuwlogin.callbacks.Constant;
import com.devandroid.ncuwlogin.callbacks.GeneralCallback;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class LoginHelper {

	public enum HotspotType {
		UNKNOWN, NCUWLAN, NCUCSIE
	}

	private static AsyncHttpClient mClient = init();

	private static NotificationManager mNotificationManager;
	private static NotificationCompat.Builder mBuilder;

	private static AsyncHttpClient init() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Connection", "Keep-Alive");
		client.setTimeout(30000);
		client.setEnableRedirects(false);
		return client;
	}

	public static HotspotType getHotspotType(@Nullable String ssid) {
		if (ssid != null) {
			switch (ssid) {
				case "NCUWL":
				case "TANetRoaming":
					return HotspotType.NCUWLAN;
				case "NCU-CSIE":
					return HotspotType.NCUCSIE;
				default:
					return HotspotType.UNKNOWN;
			}
		} else {
			return null;
		}
	}

	public static void login(final Context context, String url, RequestParams requestParams,
	                         final GeneralCallback callback) {
		String currentSsid = Utils.getCurrentSsid(context);
		HotspotType hotspotType = getHotspotType(currentSsid);

		if (currentSsid == null || hotspotType == HotspotType.UNKNOWN) {
			if (currentSsid == null) {
				currentSsid = context.getString(R.string.no_wifi_connection);
			}
			if (callback != null) {
				callback.onFail(
						String.format(context.getString(R.string.ssid_no_support), currentSsid));
			}
			return;
		}

		mNotificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle(context.getString(R.string.app_name)).setContentText(
				String.format(context.getString(R.string.login_to_ssid), currentSsid))
				.setSmallIcon(R.drawable.ic_stat_login).setProgress(0, 0, true).setOngoing(true);
		mNotificationManager.notify(Constant.NOTIFICATION_LOGIN_ID, mBuilder.build());

		mClient.post(context, url, requestParams, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] response) {

				String resultString;
				if (statusCode == 200) {
					resultString = context.getString(R.string.login_sucessful);
					if (callback != null) {
						callback.onSuccess();
					}
				} else {
					resultString = "Status: " + statusCode;
					if (callback != null) {
						callback.onFail(resultString);
					}
				}

				mBuilder.setContentTitle(context.getString(R.string.app_name))
						.setContentText(resultString).setSmallIcon(R.drawable.ic_stat_login)
						.setContentIntent(getDefaultPendingIntent(context)).setProgress(0, 0, false)
						.setOngoing(true);
				mNotificationManager.notify(Constant.NOTIFICATION_LOGIN_ID, mBuilder.build());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] errorResponse,
			                      Throwable e) {
				e.printStackTrace();

				boolean alreadyLoggedIn = false;
				String resultString, resultDetailString = "Connection problem.";

				if (headers != null) {
					resultDetailString = "";
					for (Header header : headers) {
						resultDetailString += header.toString() + "\n";
					}
				}

				if (resultDetailString.contains("Access denied")) {
					resultString = context.getString(R.string.already_logged_in);
					alreadyLoggedIn = true;
				} else {
					resultString = context.getString(R.string.failed_to_login);
				}

				mBuilder.setContentTitle(context.getString(R.string.app_name))
						.setContentText(resultString).setSmallIcon(R.drawable.ic_stat_login)
						.setContentIntent(getDefaultPendingIntent(context)).setProgress(0, 0, false)
						.setOngoing(true);
				if (alreadyLoggedIn) {
					if (callback != null) {
						callback.onFail(resultString);
					}
				} else {
					if (callback != null) {
						callback.onFail(resultString + "\n" + resultDetailString);
					}
					// Show error details in the expanded notification
					mBuilder.setStyle(new NotificationCompat.BigTextStyle()
							.bigText(resultString + "\n" + resultDetailString));
				}

				mNotificationManager.notify(Constant.NOTIFICATION_LOGIN_ID, mBuilder.build());
			}
		});
	}

	public static void logout(final Context context, final GeneralCallback callback) {
		String currentSsid = Utils.getCurrentSsid(context);
		HotspotType hotspotType = getHotspotType(currentSsid);

		if (currentSsid == null || hotspotType == HotspotType.UNKNOWN) {
			if (currentSsid == null) {
				currentSsid = context.getString(R.string.no_wifi_connection);
			}
			if (callback != null) {
				callback.onFail(
						String.format(context.getString(R.string.ssid_no_support), currentSsid));
			}
			return;
		}

		mClient.post("https://securelogin.arubanetworks.com/cgi-bin/login?cmd=logout",
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers, byte[] response) {
						if (statusCode == 200) {
							callback.onSuccess();
							mNotificationManager.cancel(Constant.NOTIFICATION_LOGIN_ID);
						} else {
							callback.onFail("Status: " + statusCode);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers, byte[] errorResponse,
					                      Throwable e) {
						String resultString, resultDetailString = "";

						if (headers != null) {
							for (Header header : headers) {
								resultDetailString += header.toString() + "\n";
							}
						}

						if (resultDetailString.contains("Access denied")) {
							resultString = context.getText(R.string.already_logged_out).toString();
						} else {
							resultString = context.getText(R.string.failed_to_logout).toString();
						}

						if (callback != null) {
							callback.onFail(resultString);
						}
					}
				});
	}

	private static PendingIntent getDefaultPendingIntent(Context context) {
		Intent notificationIntent = new Intent(context, MainActivity.class);
		return PendingIntent.getActivity(context, 0, notificationIntent, 0);
	}
}
