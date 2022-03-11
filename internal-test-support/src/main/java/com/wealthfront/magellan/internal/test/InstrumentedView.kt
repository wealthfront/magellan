package com.wealthfront.magellan.internal.test

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View

public class InstrumentedView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : View(context, attrs) {

  public var onSaveInstanceStateCount: Int = 0
  public var onRestoreInstanceStateCount: Int = 0

  override fun onSaveInstanceState(): Parcelable? {
    onSaveInstanceStateCount += 1
    return super.onSaveInstanceState()
  }

  override fun onRestoreInstanceState(state: Parcelable?) {
    onRestoreInstanceStateCount += 1
    super.onRestoreInstanceState(state)
  }
}
