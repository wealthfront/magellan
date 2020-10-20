package com.wealthfront.magellan.navigation

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import com.wealthfront.magellan.core.Displayable
import com.wealthfront.magellan.lifecycle.LifecycleAware

public interface NavigableCompat : LifecycleAware, Displayable {

  public val activity: Activity?

  /**
   * @return the title to be displayed on the navigable
   */
  @JvmDefault
  public fun getTitle(context: Context): String = ""

  /**
   * Set the title to be displayed on the navigable
   */
  @JvmDefault
  public fun setTitle(@StringRes titleResId: Int) {
    activity?.setTitle(titleResId)
  }

  /**
   * Set the title to be displayed on the navigable
   */
  @JvmDefault
  public fun setTitle(title: CharSequence) {
    activity?.title = title
  }
}
