package com.wealthfront.magellan.core

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.LinearNavigator

abstract class Journey<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer
) : Screen<V>(createBinding) {

  protected var navigator by lifecycle(LinearNavigator { viewBinding!!.container() })
}
