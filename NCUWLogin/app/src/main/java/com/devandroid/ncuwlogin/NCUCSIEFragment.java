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
import com.loopj.android.http.RequestParams;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NCUCSIEFragment extends Fragment implements View.OnClickListener {

	@InjectView(R.id.button_login) Button mLoginButton;
	@InjectView(R.id.editText_user) EditText mUsernameEditText;
	@InjectView(R.id.editText_password) EditText mPasswordEditText;
	@InjectView(R.id.textView_debug) TextView mDebugTextView;

	private MainActivity mMainActivity;

	private static final String USER_KEY = Constant.MEMORY_KEY_CSIE_USER;
	private static final String PASS_KEY = Constant.MEMORY_KEY_CSIE_PASSWORD;

	public static NCUCSIEFragment newInstance() {
		return new NCUCSIEFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_ncucsie, container, false);
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
		}
	}

	private void saveAndLogin() {
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
	}

	public static void login(Context context, GeneralCallback callback) {
		String username = Memory.getString(context, USER_KEY, "");
		String password = Memory.getString(context, PASS_KEY, "");

		RequestParams params = new RequestParams();
		params.put("login[type]", 0);
		params.put("login[username]", username);
		params.put("login[password]", password);

		String url = "https://10.115.50.254";

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
