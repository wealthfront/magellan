package com.wealthfront.magellan

import android.view.LayoutInflater
import android.view.Menu
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.lifecycle

public abstract class LegacyExpedition<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer
) : Step<V>(createBinding) {

  public var navigator: LegacyNavigator by lifecycle(LegacyNavigator(this) { viewBinding!!.container() })

  public fun setMenu(menu: Menu) {
    navigator.menu = menu
  }

  override fun toString(): String = this.javaClass.simpleName
}
