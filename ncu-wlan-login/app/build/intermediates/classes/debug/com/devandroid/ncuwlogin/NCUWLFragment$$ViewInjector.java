// Generated code from Butter Knife. Do not modify!
package com.devandroid.ncuwlogin;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class NCUWLFragment$$ViewInjector<T extends com.devandroid.ncuwlogin.NCUWLFragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361900, "field 'mLoginButton'");
    target.mLoginButton = finder.castView(view, 2131361900, "field 'mLoginButton'");
    view = finder.findRequiredView(source, 2131361902, "field 'mLogoutButton'");
    target.mLogoutButton = finder.castView(view, 2131361902, "field 'mLogoutButton'");
    view = finder.findRequiredView(source, 2131361897, "field 'mUsernameEditText'");
    target.mUsernameEditText = finder.castView(view, 2131361897, "field 'mUsernameEditText'");
    view = finder.findRequiredView(source, 2131361898, "field 'mPasswordEditText'");
    target.mPasswordEditText = finder.castView(view, 2131361898, "field 'mPasswordEditText'");
    view = finder.findRequiredView(source, 2131361901, "field 'mDebugTextView'");
    target.mDebugTextView = finder.castView(view, 2131361901, "field 'mDebugTextView'");
  }

  @Override public void reset(T target) {
    target.mLoginButton = null;
    target.mLogoutButton = null;
    target.mUsernameEditText = null;
    target.mPasswordEditText = null;
    target.mDebugTextView = null;
  }
}
