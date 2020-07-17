package com.wealthfront.magellan

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * The container to be used to display the screens using the [Navigator]. Must have the id
 * `magellan_container`. This will also block touch events automatically during navigation to avoid accidental
 * double taps. This view prevents adding views in XML. When views are adding programmatically, initially [addView]
 * with the @param isAddedProgrammatically must be used to initialize the view.
 */
class ScreenContainer @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  var interceptTouchEvents = false
  private var isChildAddedProgrammatically = false

  override fun onInterceptTouchEvent(ev: MotionEvent) = interceptTouchEvents

  override fun onDetachedFromWindow() {
    isChildAddedProgrammatically = false
    super.onDetachedFromWindow()
  }

  fun addView(child: View, isAddedProgrammatically: Boolean) {
    isChildAddedProgrammatically = isAddedProgrammatically
    super.addView(child)
  }

  fun addView(child: View, index: Int, isAddedProgrammatically: Boolean) {
    isChildAddedProgrammatically = isAddedProgrammatically
    super.addView(child, index)
  }

  override fun addView(
    child: View,
    index: Int,
    params: ViewGroup.LayoutParams
  ) {
    if (!isChildAddedProgrammatically) {
      throw UnsupportedOperationException("ScreenContainer cannot have any children added in XML.")
    }
    super.addView(child, index, params)
  }
}
