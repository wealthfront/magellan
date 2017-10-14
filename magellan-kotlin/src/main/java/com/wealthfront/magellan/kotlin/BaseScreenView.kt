package com.wealthfront.magellan.kotlin

import android.content.Context
import android.view.View
import android.widget.FrameLayout

/**
 * Base class to easily implement a [ScreenView]. Inherit from [FrameLayout].
 */
class BaseScreenView<S>(context: Context) : FrameLayout(context), ScreenView<S> where S : Screen<*> {

  private lateinit var screen: S

  override fun setScreen(screen: S) {
    this.screen = screen
  }

  override fun getScreen(): S = screen

  fun inflate(resource: Int): View = View.inflate(context, resource, this)

}
