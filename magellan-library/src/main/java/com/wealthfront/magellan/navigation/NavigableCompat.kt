package com.wealthfront.magellan.navigation

import android.app.Activity
import android.content.Context
import android.view.Menu
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.wealthfront.magellan.core.Displayable
import com.wealthfront.magellan.lifecycle.LifecycleAware
import org.jetbrains.annotations.NotNull

const val DEFAULT_ACTION_BAR_COLOR_RES = 0

interface NavigableCompat : LifecycleAware, Displayable {

  val activity: Activity?

  /**
   * Override this method to dynamically change the menu.
   */
  @JvmDefault
  fun onUpdateMenu(@NotNull menu: Menu) {}

  /**
   * @return true if we should show the ActionBar, false otherwise (true by default).
   */
  @JvmDefault
  fun shouldShowActionBar(): Boolean = true

  /**
   * @return true if we should animate the ActionBar, false otherwise (true by default).
   */
  @JvmDefault
  fun shouldAnimateActionBar(): Boolean = true

  /**
   * @return the title to be displayed on the navigable
   */
  @JvmDefault
  fun getTitle(@NotNull context: Context) = ""

  /**
   * @return the color of the ActionBar (invalid by default).
   */
  @ColorRes
  @JvmDefault
  fun getActionBarColorRes(): Int = DEFAULT_ACTION_BAR_COLOR_RES

  /**
   * Finish the Activity, and therefore quit the app in a Single Activity Architecture.
   */
  @JvmDefault
  fun quit(): Boolean {
    activity?.finish()
    return true
  }

  @JvmDefault
  fun setTitle(@StringRes titleResId: Int) {
    activity?.setTitle(titleResId)
  }

  @JvmDefault
  fun setTitle(title: CharSequence) {
    activity?.title = title
  }
}
