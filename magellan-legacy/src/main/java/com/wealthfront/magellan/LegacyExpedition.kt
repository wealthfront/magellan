package com.wealthfront.magellan

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.CurrentNavigableProvider

public abstract class LegacyExpedition<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer
) : Step<V>(createBinding) {

  public var navigator: Navigator by lifecycle(Navigator { viewBinding!!.container() })

  public fun setCurrentNavProvider(currentNavigableProvider: CurrentNavigableProvider) {
    attachToLifecycle(currentNavigableProvider)
    navigator.currentNavigableProvider = currentNavigableProvider
  }

}
