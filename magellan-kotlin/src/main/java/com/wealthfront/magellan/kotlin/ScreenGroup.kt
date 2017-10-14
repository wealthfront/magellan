package com.wealthfront.magellan.kotlin

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import java.util.ArrayList

/**
 * A Screen containing a list of screens. Useful to display reusable Screens that can be either in another one or on
 * it's own.
 */
abstract class ScreenGroup<S : Screen<*>, V>(screens: List<S>) : Screen<V>() where V : ViewGroup, V : ScreenView<*> {

  private val screens: MutableList<S> = ArrayList(screens)

  fun addScreen(screen: S) {
    this.checkOnCreateNotYetCalled("Cannot add screen after onCreate is called")
    screen.checkOnCreateNotYetCalled("Cannot add a screen after onCreate is called on the screen")
    screens.add(screen)
  }

  fun addScreens(screens: List<S>) {
    for (screen in screens) {
      addScreen(screen)
    }
  }

  override fun onShow(context: Context) {
    for (screen in screens) {
      screen.recreateView(screen.activity, screen.navigator)
      screen.createDialog()
      screen.onShow(context)
    }
  }

  override fun onRestore(savedInstanceState: Bundle) {
    for (screen in screens) {
      screen.onRestore(savedInstanceState)
    }
  }

  override fun onResume(context: Context) {
    for (screen in screens) {
      screen.onResume(context)
    }
  }

  override fun onPause(context: Context) {
    for (screen in screens) {
      screen.onPause(context)
    }
  }

  override fun onSave(outState: Bundle) {
    for (screen in screens) {
      screen.onSave(outState)
    }
  }

  override fun onHide(context: Context) {
    for (screen in screens) {
      screen.onHide(context)
      screen.destroyDialog()
      screen.destroyView()
    }
  }

  fun getScreens(): List<S> = ArrayList(screens)

}
