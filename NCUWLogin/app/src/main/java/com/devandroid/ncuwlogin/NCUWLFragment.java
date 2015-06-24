package com.devandroid.ncuwlogin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.devandroid.ncuwlogin.callbacks.Constant;
import com.devandroid.ncuwlogin.callbacks.GeneralCallback;
import com.devandroid.ncuwlogin.callbacks.Memory;
import com.devandroid.ncuwlogin.libs.LoginHelper;
import com.devandroid.ncuwlogin.libs.Utils;
import com.loopj.android.http.RequestParams;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NCUWLFragment extends Fragment implements View.OnClickListener {

	@InjectView(R.id.button_login) Button mLoginButton;
	@InjectView(R.id.button_logout) Button mLogoutButton;
	@InjectView(R.id.editText_user) EditText mUsernameEditText;
	@InjectView(R.id.editText_password) EditText mPasswordEditText;
	@InjectView(R.id.textView_debug) TextView mDebugTextView;

	private MainActivity mMainActivity;

	private static final String USER_KEY = Constant.MEMORY_KEY_USER;
	private static final String PASS_KEY = Constant.MEMORY_KEY_PASSWORD;

	public static NCUWLFragment newInstance() {
		return new NCUWLFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ncuwl, container, false);
		ButterKnife.inject(this, view);

		initValues();
		setUpViews();

		return view;
	}

	private void initValues() {
		mMainActivity = (MainActivity) getActivity();
	}

	private void setUpViews() {
		mLoginButton.setOnClickListener(this);
		mLogoutButton.setOnClickListener(this);
		mUsernameEditText.setText(Memory.getString(mMainActivity, USER_KEY, ""));
		mPasswordEditText.setText(Memory.getString(mMainActivity, PASS_KEY, ""));
		mPasswordEditText.setImeActionLabel(getText(R.string.ime_submit), KeyEvent.KEYCODE_ENTER);
		mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				saveAndLogin();
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == mLoginButton) {
			saveAndLogin();
		} else if (v == mLogoutButton) {
			LoginHelper.logoutNCUWL(mMainActivity, new GeneralCallback() {

				@Override
				public void onSuccess() {
					showMessage(R.string.logout_sucessful);
				}

				@Override
				public void onFail(String reason) {
					showMessage(reason);
				}
			});
		}
	}

	private void saveAndLogin() {
		String ssid = Utils.getCurrentSsid(mMainActivity);
		LoginHelper.HotspotType hotspotType = LoginHelper.getHotspotType(ssid);
		if (hotspotType == LoginHelper.HotspotType.NCUWLAN) {
			String username = mUsernameEditText.getText().toString();
			String password = mPasswordEditText.getText().toString();
			Memory.setString(mMainActivity, USER_KEY, username);
			Memory.setString(mMainActivity, PASS_KEY, password);

			login(mMainActivity, new GeneralCallback() {

				@Override
				public void onSuccess() {
					showMessage(R.string.login_sucessful);
				}

				@Override
				public void onFail(String reason) {
					showMessage(reason);
				}
			});
		} else {
			showMessage(String.format(getString(R.string.ssid_no_support), ssid));
		}
	}

	public static void login(Context context, GeneralCallback callback) {
		String username = Memory.getString(context, USER_KEY, "");
		String password = Memory.getString(context, PASS_KEY, "");

		RequestParams params = new RequestParams();
		params.put("user", username);
		params.put("password", password);

		String url = "https://securelogin.arubanetworks.com/auth/index.html/u";

		LoginHelper.login(context, url, params, callback);
	}

	private void showMessage(int messageRes) {
		mDebugTextView.setVisibility(View.VISIBLE);
		mDebugTextView.setText(getText(messageRes));
	}

	private void showMessage(CharSequence message) {
		mDebugTextView.setVisibility(View.VISIBLE);
		mDebugTextView.setText(message);
	}

}
