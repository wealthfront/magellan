package com.wealthfront.magellan.core

import android.view.LayoutInflater
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PROTECTED
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.navigation.LinearNavigator

public abstract class Journey<V : ViewBinding>(
  inflateBinding: (LayoutInflater) -> V,
  getContainer: V.() -> ScreenContainer
) : Step<V>(inflateBinding) {

  @VisibleForTesting(otherwise = PROTECTED) public var navigator: LinearNavigator by attachFieldToLifecycle(LinearNavigator { viewBinding!!.getContainer() })
}
