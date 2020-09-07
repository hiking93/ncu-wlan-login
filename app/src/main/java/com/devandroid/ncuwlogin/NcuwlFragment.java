package com.devandroid.ncuwlogin;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.devandroid.ncuwlogin.callbacks.Constant;
import com.devandroid.ncuwlogin.callbacks.GeneralCallback;
import com.devandroid.ncuwlogin.callbacks.Memory;
import com.devandroid.ncuwlogin.libs.LoginHelper;
import com.devandroid.ncuwlogin.libs.Utils;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NcuwlFragment extends Fragment implements View.OnClickListener {

    private static final String USER_KEY = Constant.MEMORY_KEY_USER;
    private static final String PASS_KEY = Constant.MEMORY_KEY_PASSWORD;

    private static final String LOGIN_URL =
            "https://securelogin.arubanetworks.com/auth/index.html/u";

    public static NcuwlFragment newInstance() {
        return new NcuwlFragment();
    }

    @BindView(R.id.button_login)
    Button mLoginButton;
    @BindView(R.id.button_logout)
    Button mLogoutButton;
    @BindView(R.id.editText_user)
    EditText mUsernameEditText;
    @BindView(R.id.editText_password)
    EditText mPasswordEditText;
    @BindView(R.id.progressBar_login)
    ProgressBar mProgressBar;
    @BindView(R.id.textView_debug)
    TextView mDebugTextView;

    private MainActivity mMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ncuwl, container, false);
        ButterKnife.bind(this, view);

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

            setOnProgress(true);
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

        LoginHelper.login(context, LOGIN_URL, params, callback);
    }

    private void setOnProgress(boolean onProgress) {
        mDebugTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(onProgress ? View.VISIBLE : View.GONE);
    }

    private void showMessage(int messageRes) {
        showMessage(getText(messageRes));
    }

    private void showMessage(CharSequence message) {
        setOnProgress(false);
        mDebugTextView.setVisibility(View.VISIBLE);
        mDebugTextView.setText(message);
    }
}
