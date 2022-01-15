package com.wealthfront.magellan

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.navigation.CurrentNavigableProvider

public abstract class LegacyExpedition<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer
) : Step<V>(createBinding) {

  public var navigator: Navigator by attachFieldToLifecycle(
    Navigator({ viewBinding!!.container() }, Magellan.navigationRequestHandler)
  )

  public fun setCurrentNavProvider(currentNavigableProvider: CurrentNavigableProvider) {
    navigator.currentNavigableProvider = currentNavigableProvider
  }
}
