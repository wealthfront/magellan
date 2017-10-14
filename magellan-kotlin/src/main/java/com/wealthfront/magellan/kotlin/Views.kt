package com.wealthfront.magellan.kotlin

import android.view.View
import android.view.ViewTreeObserver

class Views private constructor() {

  init {
    throw AssertionError()
  }

  companion object {

    fun whenMeasured(view: View, onMeasured: OnMeasured) {
      val width = view.width
      val height = view.height
      if (width > 0 && height > 0) {
        onMeasured.onMeasured()
        return
      }

      view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
          val observer = view.viewTreeObserver
          if (observer.isAlive) {
            observer.removeOnPreDrawListener(this)
          }
          onMeasured.onMeasured()
          return true
        }
      })
    }
  }


  interface OnMeasured {
    fun onMeasured()
  }


}
