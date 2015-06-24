// Generated code from Butter Knife. Do not modify!
package com.devandroid.ncuwlogin;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class NCUCSIEFragment$$ViewInjector<T extends com.devandroid.ncuwlogin.NCUCSIEFragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361900, "field 'mLoginButton'");
    target.mLoginButton = finder.castView(view, 2131361900, "field 'mLoginButton'");
    view = finder.findRequiredView(source, 2131361897, "field 'mUsernameEditText'");
    target.mUsernameEditText = finder.castView(view, 2131361897, "field 'mUsernameEditText'");
    view = finder.findRequiredView(source, 2131361898, "field 'mPasswordEditText'");
    target.mPasswordEditText = finder.castView(view, 2131361898, "field 'mPasswordEditText'");
    view = finder.findRequiredView(source, 2131361899, "field 'mTypeSpinner'");
    target.mTypeSpinner = finder.castView(view, 2131361899, "field 'mTypeSpinner'");
    view = finder.findRequiredView(source, 2131361901, "field 'mDebugTextView'");
    target.mDebugTextView = finder.castView(view, 2131361901, "field 'mDebugTextView'");
  }

  @Override public void reset(T target) {
    target.mLoginButton = null;
    target.mUsernameEditText = null;
    target.mPasswordEditText = null;
    target.mTypeSpinner = null;
    target.mDebugTextView = null;
  }
}
