// Generated code from Butter Knife. Do not modify!
package com.devandroid.ncuwlogin;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class AboutActivity$$ViewInjector<T extends com.devandroid.ncuwlogin.AboutActivity> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361894, "field 'mMainListView'");
    target.mMainListView = finder.castView(view, 2131361894, "field 'mMainListView'");
  }

  @Override public void reset(T target) {
    target.mMainListView = null;
  }
}
