package com.wealthfront.magellan.kotlin

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * The container to be used to display the screens using the [Navigator]. Must have the id
 * `magellan_container`. This will also block touch events automatically during navigation to avoid accidental
 * double taps.
 */
class ScreenContainer : FrameLayout {

  private var interceptTouchEvents: Boolean = false

  constructor(context: Context) : super(context) {}

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = interceptTouchEvents

  fun setInterceptTouchEvents(interceptTouchEvents: Boolean) {
    this.interceptTouchEvents = interceptTouchEvents
  }

}