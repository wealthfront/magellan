package com.wealthfront.magellan;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * The container to be used to display the screens using the {@link com.wealthfront.magellan.navigation.Navigator}. Must have the id
 * {@code magellan_container}. This will also block touch events automatically during navigation to avoid accidental
 * double taps.
 */
public class ScreenContainer extends FrameLayout {

  private boolean interceptTouchEvents;

  public ScreenContainer(Context context) {
    super(context);
  }

  public ScreenContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ScreenContainer(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return interceptTouchEvents;
  }

  public void setInterceptTouchEvents(boolean interceptTouchEvents) {
    this.interceptTouchEvents = interceptTouchEvents;
  }

  @Override
  protected void dispatchRestoreInstanceState(
      SparseArray<Parcelable> container) {
    dispatchThawSelfOnly(container);
  }

  @Override
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
    dispatchFreezeSelfOnly(container);
  }
}
