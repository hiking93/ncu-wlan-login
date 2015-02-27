// Generated code from Butter Knife. Do not modify!
package com.devandroid.ncuwlogin;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final com.devandroid.ncuwlogin.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296322, "field 'mLoginButton'");
    target.mLoginButton = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131296323, "field 'mLogoutButton'");
    target.mLogoutButton = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131296320, "field 'mUsernameEditText'");
    target.mUsernameEditText = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296321, "field 'mPasswordEditText'");
    target.mPasswordEditText = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296324, "field 'mDebugTextView'");
    target.mDebugTextView = (android.widget.TextView) view;
  }

  public static void reset(com.devandroid.ncuwlogin.MainActivity target) {
    target.mLoginButton = null;
    target.mLogoutButton = null;
    target.mUsernameEditText = null;
    target.mPasswordEditText = null;
    target.mDebugTextView = null;
  }
}
