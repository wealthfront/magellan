package com.wealthfront.magellan.navigation

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import com.wealthfront.magellan.core.Displayable
import com.wealthfront.magellan.lifecycle.LifecycleAware
import org.jetbrains.annotations.NotNull

interface NavigableCompat : LifecycleAware, Displayable {

  val activity: Activity?

  /**
   * @return the title to be displayed on the navigable
   */
  @JvmDefault
  fun getTitle(@NotNull context: Context) = ""

  /**
   * Set the title to be displayed on the navigable
   */
  @JvmDefault
  fun setTitle(@StringRes titleResId: Int) {
    activity?.setTitle(titleResId)
  }

  /**
   * Set the title to be displayed on the navigable
   */
  @JvmDefault
  fun setTitle(title: CharSequence) {
    activity?.title = title
  }
}
