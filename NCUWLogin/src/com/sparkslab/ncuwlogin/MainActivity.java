package com.sparkslab.ncuwlogin;

import com.sparkslab.ncuwlogin.callbacks.Constant;
import com.sparkslab.ncuwlogin.callbacks.GeneralCallback;
import com.sparkslab.ncuwlogin.callbacks.Memory;
import com.sparkslab.ncuwlogin.libs.LoginHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	@InjectView(R.id.button_login)
	Button mLoginButton;

	@InjectView(R.id.button_logout)
	Button mLogoutButton;

	@InjectView(R.id.editText_user)
	EditText mUsernameEditText;

	@InjectView(R.id.editText_password)
	EditText mPasswordEditText;

	@InjectView(R.id.textView_debug)
	TextView mDebugTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.inject(this);
		setUpViews();
	}

	private void setUpViews() {
		mLoginButton.setOnClickListener(this);
		mLogoutButton.setOnClickListener(this);
		mUsernameEditText.setText(Memory.getString(this,
				Constant.MEMORY_KEY_USER, ""));
		mPasswordEditText.setText(Memory.getString(this,
				Constant.MEMORY_KEY_PASSWORD, ""));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v == mLoginButton) {
			Memory.setString(this, Constant.MEMORY_KEY_USER, mUsernameEditText
					.getText().toString());
			Memory.setString(this, Constant.MEMORY_KEY_PASSWORD,
					mPasswordEditText.getText().toString());
			LoginHelper.login(this, mUsernameEditText.getText().toString(),
					mPasswordEditText.getText().toString(),
					new GeneralCallback() {

						@Override
						public void onSuccess() {
							mDebugTextView.setText("Login successful.");
						}

						@Override
						public void onFail(String reason) {
							mDebugTextView.setText(reason);
						}
					});
		} else if (v == mLogoutButton) {
			LoginHelper.logout(this, new GeneralCallback() {

				@Override
				public void onSuccess() {
					mDebugTextView.setText("Logout successful.");
				}

				@Override
				public void onFail(String reason) {
					mDebugTextView.setText(reason);
				}
			});
		}
	}
}
