package com.wealthfront.magellan.support;

import android.support.v4.app.ActivityCompat;
import android.view.ViewGroup;
import com.wealthfront.magellan.Screen;
import com.wealthfront.magellan.ScreenView;

public abstract class SingleScreen<V extends ViewGroup & ScreenView> extends Screen<V> {
  @Override
  public final void requestPermission(String[] permissions, int requestCode) {
   Preconditions.checkNotNull(getActivity(), "Needs non null activity");
   getNavigator().registerForPermissionResultWithId(requestCode, this);
   ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
  }
}
