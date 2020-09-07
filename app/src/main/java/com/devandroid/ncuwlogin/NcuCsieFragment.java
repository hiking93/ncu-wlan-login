package com.devandroid.ncuwlogin;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.devandroid.ncuwlogin.callbacks.Constant;
import com.devandroid.ncuwlogin.callbacks.GeneralCallback;
import com.devandroid.ncuwlogin.callbacks.Memory;
import com.devandroid.ncuwlogin.libs.IgnoreSSLSocketFactory;
import com.devandroid.ncuwlogin.libs.LoginHelper;
import com.devandroid.ncuwlogin.libs.Utils;
import com.loopj.android.http.RequestParams;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NcuCsieFragment extends Fragment implements View.OnClickListener {

    private static final String USER_KEY = Constant.MEMORY_KEY_CSIE_USER;
    private static final String PASS_KEY = Constant.MEMORY_KEY_CSIE_PASSWORD;
    private static final String TYPE_KEY = Constant.MEMORY_KEY_CSIE_TYPE;

    private static final String LOGIN_URL = "http://10.115.51.254/";

    @BindView(R.id.button_login)
    Button mLoginButton;
    @BindView(R.id.editText_user)
    EditText mUsernameEditText;
    @BindView(R.id.editText_password)
    EditText mPasswordEditText;
    @BindView(R.id.spinner_type)
    Spinner mTypeSpinner;
    @BindView(R.id.progressBar_login)
    ProgressBar mProgressBar;
    @BindView(R.id.textView_debug)
    TextView mDebugTextView;

    private MainActivity mMainActivity;

    public static NcuCsieFragment newInstance() {
        return new NcuCsieFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ncucsie, container, false);
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

        ArrayAdapter spinnerAdapter =
                new ArrayAdapter<>(mMainActivity, R.layout.support_simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.ncucsie_type));
        mTypeSpinner.setAdapter(spinnerAdapter);
        mTypeSpinner.setSelection(Memory.getInt(mMainActivity, TYPE_KEY, 0));

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
        String ssid = Utils.getCurrentSsid(mMainActivity);
        LoginHelper.HotspotType hotspotType = LoginHelper.getHotspotType(ssid);
        if (hotspotType == LoginHelper.HotspotType.NCUCSIE) {
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            int type = mTypeSpinner.getSelectedItemPosition();
            Memory.setString(mMainActivity, USER_KEY, username);
            Memory.setString(mMainActivity, PASS_KEY, password);
            Memory.setInt(mMainActivity, TYPE_KEY, type);

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
        int type = Memory.getInt(context, TYPE_KEY, 0);

        RequestParams params = new RequestParams();
        params.put("login[type]", type);
        params.put("login[username]", username);
        params.put("login[password]", password);

        IgnoreSSLSocketFactory factory = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            factory = new IgnoreSSLSocketFactory(keyStore);
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        LoginHelper.login(context, LOGIN_URL, params, callback, factory);
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
