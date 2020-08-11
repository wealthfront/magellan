package com.wealthfront.magellan.core

import android.view.LayoutInflater
import androidx.annotation.VisibleForTesting
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.LinearNavigator

abstract class Journey<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer
) : Step<V>(createBinding) {

  protected var navigator by lifecycle(LinearNavigator { viewBinding!!.container() })
    @VisibleForTesting set

  override fun toString(): String = this.javaClass.simpleName
}
