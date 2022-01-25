package com.wealthfront.magellan.core

import android.view.LayoutInflater
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PROTECTED
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.navigation.DefaultLinearNavigator
import com.wealthfront.magellan.navigation.LinearNavigator
import com.wealthfront.magellan.navigation.NavigationRequestHandler

public abstract class Journey<V : ViewBinding>(
  inflateBinding: (LayoutInflater) -> V,
  getContainer: V.() -> ScreenContainer,
  navigationRequestHandler: NavigationRequestHandler? = Magellan.navigationRequestHandler
) : Step<V>(inflateBinding) {

  @VisibleForTesting(otherwise = PROTECTED)
  public var navigator: LinearNavigator by attachFieldToLifecycle(
    DefaultLinearNavigator({ viewBinding!!.getContainer() }, navigationRequestHandler)
  )
}
