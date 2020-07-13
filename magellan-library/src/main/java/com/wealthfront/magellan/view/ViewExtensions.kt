package com.wealthfront.magellan.view

import android.view.View
import android.view.ViewTreeObserver

internal fun View.whenMeasured(onMeasured: () -> Unit) {
  val width = width
  val height = height
  if (width > 0 && height > 0) {
    onMeasured.invoke()
    return
  }

  viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
    override fun onPreDraw(): Boolean {
      val observer = viewTreeObserver
      if (observer.isAlive) {
        observer.removeOnPreDrawListener(this)
      }
      onMeasured.invoke()
      return true
    }
  })
}