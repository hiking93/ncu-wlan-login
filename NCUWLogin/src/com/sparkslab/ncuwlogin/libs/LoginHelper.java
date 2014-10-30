package com.sparkslab.ncuwlogin.libs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import com.sparkslab.ncuwlogin.MainActivity;
import com.sparkslab.ncuwlogin.R;
import com.sparkslab.ncuwlogin.callbacks.Constant;
import com.sparkslab.ncuwlogin.callbacks.GeneralCallback;

public class LoginHelper {
	private static AsyncHttpClient mClient = init();

	private static NotificationManager mNotificationManager;
	private static NotificationCompat.Builder mBuilder;

	private static String expectedSsid = Constant.EXPECTED_SSID;

	private static AsyncHttpClient init() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.addHeader("Connection", "Keep-Alive");
		// client.addHeader("User-Agent", USER_AGENT);
		client.setTimeout(30000);
		client.setEnableRedirects(false);
		return client;
	}

	public static void login(final Context context, String user,
			String password, final GeneralCallback callback) {
		String currentSsid = Utils.getCurrentSsid(context);
		if (currentSsid == null || !currentSsid.equals(expectedSsid)) {
			if (callback != null) {
				callback.onFail(String.format(context
						.getString(R.string.youre_not_connected_to_ssid_ssid),
						expectedSsid, currentSsid));
			}
			return;
		}

		RequestParams params = new RequestParams();
		params.put("user", user);
		params.put("password", password);

		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle(context.getString(R.string.app_name))
				.setContentText(
						String.format(
								context.getString(R.string.login_to_ssid),
								expectedSsid))
				.setSmallIcon(R.drawable.ic_stat_login).setProgress(0, 0, true)
				.setOngoing(true);
		mNotificationManager.notify(Constant.NOTIFICATION_LOGIN_ID,
				mBuilder.build());

		mClient.post(context,
				"https://securelogin.arubanetworks.com/auth/index.html/u",
				params, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {

						String resultString = "";
						if (statusCode == 200) {
							resultString = context
									.getString(R.string.login_sucessful);
							if (callback != null) {
								callback.onSuccess();
							}
						} else {
							resultString = "Status: " + statusCode;
							if (callback != null) {
								callback.onFail(resultString);
							}
						}

						mBuilder.setContentTitle(
								context.getString(R.string.app_name))
								.setContentText(resultString)
								.setSmallIcon(R.drawable.ic_stat_login)
								.setContentIntent(
										getDefaultPendingIntent(context))
								.setProgress(0, 0, false).setOngoing(true);
						mNotificationManager.notify(
								Constant.NOTIFICATION_LOGIN_ID,
								mBuilder.build());
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						e.printStackTrace();

						boolean alreadyLoggedIn = false;
						String resultString = "", resultDetailString = "Connection problem.";

						if (headers != null) {
							resultDetailString = "";
							for (Header header : headers) {
								resultDetailString += header.toString() + "\n";
							}
						}

						if (resultDetailString.contains("Access denied")) {
							resultString = context
									.getString(R.string.already_logged_in);
							alreadyLoggedIn = true;
						} else {
							resultString = context
									.getString(R.string.failed_to_login);
						}

						mBuilder.setContentTitle(
								context.getString(R.string.app_name))
								.setContentText(resultString)
								.setSmallIcon(R.drawable.ic_stat_login)
								.setContentIntent(
										getDefaultPendingIntent(context))
								.setProgress(0, 0, false).setOngoing(true);
						if (alreadyLoggedIn) {
							if (callback != null) {
								callback.onFail(resultString);
							}
						} else {
							if (callback != null) {
								callback.onFail(resultString + "\n"
										+ resultDetailString);
							}
							// Show error details in the expanded notification
							mBuilder.setStyle(new NotificationCompat.BigTextStyle()
									.bigText(resultString + "\n"
											+ resultDetailString));
						}

						mNotificationManager.notify(
								Constant.NOTIFICATION_LOGIN_ID,
								mBuilder.build());
					}
				});
	}

	public static void logout(final Context context,
			final GeneralCallback callback) {
		String currentSsid = Utils.getCurrentSsid(context);
		if (currentSsid == null || !currentSsid.equals(expectedSsid)) {
			if (callback != null) {
				callback.onFail(String.format(context
						.getString(R.string.youre_not_connected_to_ssid_ssid),
						expectedSsid, currentSsid));
			}
			return;
		}

		mClient.post(
				"https://securelogin.arubanetworks.com/cgi-bin/login?cmd=logout",
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						if (statusCode == 200) {
							callback.onSuccess();
							mNotificationManager
									.cancel(Constant.NOTIFICATION_LOGIN_ID);
						} else {
							callback.onFail("Status: " + statusCode);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						String resultString = "", resultDetailString = "";

						if (headers != null) {
							for (Header header : headers) {
								resultDetailString += header.toString() + "\n";
							}
						}

						if (resultDetailString.contains("Access denied")) {
							resultString = context.getText(
									R.string.already_logged_out).toString();
						} else {
							resultString = context.getText(
									R.string.failed_to_logout).toString();
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
