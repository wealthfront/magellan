package com.wealthfront.magellan.core

import android.view.LayoutInflater
import android.view.Menu
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.LinearNavigator

public abstract class Journey<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer
) : Step<V>(createBinding) {

  protected var navigator: LinearNavigator by lifecycle(LinearNavigator(this) { viewBinding!!.container() })

  public fun setMenu(menu: Menu) {
    navigator.menu = menu
  }

  override fun toString(): String = this.javaClass.simpleName
}
