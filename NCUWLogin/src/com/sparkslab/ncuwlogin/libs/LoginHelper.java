package com.sparkslab.ncuwlogin.libs;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import com.sparkslab.ncuwlogin.R;
import com.sparkslab.ncuwlogin.callbacks.Constant;
import com.sparkslab.ncuwlogin.callbacks.GeneralCallback;

public class LoginHelper {
	private static AsyncHttpClient mClient = init();

	private static NotificationManager mNotificationManager;
	private static NotificationCompat.Builder mBuilder;

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
		RequestParams params = new RequestParams();
		params.put("user", user);
		params.put("password", password);

		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle(context.getString(R.string.app_name))
				.setContentText("Log in to NCUWL...")
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
							resultString = "Logged in.";
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
								.setProgress(0, 0, false).setOngoing(false);
						mNotificationManager.notify(
								Constant.NOTIFICATION_LOGIN_ID,
								mBuilder.build());
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						e.printStackTrace();

						boolean alreadyLoggedIn = false;
						String resultString = "", resultDetailString = "";

						for (Header header : headers) {
							resultDetailString += header.toString() + "\n";
						}

						if (resultDetailString.contains("Access denied")) {
							resultString = "Already logged in.";
							alreadyLoggedIn = true;
						} else {
							resultString = "Failed to login.";
						}

						mBuilder.setContentTitle(
								context.getString(R.string.app_name))
								.setContentText(resultString)
								.setSmallIcon(R.drawable.ic_stat_login)
								.setProgress(0, 0, false).setOngoing(false);
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

	public static void logout(Context context, final GeneralCallback callback) {
		mClient.post(
				"https://securelogin.arubanetworks.com/cgi-bin/login?cmd=logout",
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						if (statusCode == 200) {
							callback.onSuccess();
						} else {
							callback.onFail("Status: " + statusCode);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						callback.onFail("Post failed: "
								+ e.getLocalizedMessage());
					}
				});
	}
}
