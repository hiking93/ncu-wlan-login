// Generated code from Butter Knife. Do not modify!
package com.devandroid.ncuwlogin;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class NCUCSIEFragment$$ViewInjector<T extends com.devandroid.ncuwlogin.NCUCSIEFragment> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296363, "field 'mLoginButton'");
    target.mLoginButton = finder.castView(view, 2131296363, "field 'mLoginButton'");
    view = finder.findRequiredView(source, 2131296361, "field 'mUsernameEditText'");
    target.mUsernameEditText = finder.castView(view, 2131296361, "field 'mUsernameEditText'");
    view = finder.findRequiredView(source, 2131296362, "field 'mPasswordEditText'");
    target.mPasswordEditText = finder.castView(view, 2131296362, "field 'mPasswordEditText'");
    view = finder.findRequiredView(source, 2131296364, "field 'mDebugTextView'");
    target.mDebugTextView = finder.castView(view, 2131296364, "field 'mDebugTextView'");
  }

  @Override public void reset(T target) {
    target.mLoginButton = null;
    target.mUsernameEditText = null;
    target.mPasswordEditText = null;
    target.mDebugTextView = null;
  }
}
