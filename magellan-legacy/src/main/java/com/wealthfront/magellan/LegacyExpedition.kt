package com.wealthfront.magellan

import android.view.LayoutInflater
import android.view.Menu
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.lifecycle

abstract class LegacyExpedition<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer
) : Step<V>(createBinding) {

  protected var navigator by lifecycle(LegacyNavigator { viewBinding!!.container() })

  fun setMenu(menu: Menu) {
    navigator.menu = menu
  }

  override fun toString(): String = this.javaClass.simpleName
}
