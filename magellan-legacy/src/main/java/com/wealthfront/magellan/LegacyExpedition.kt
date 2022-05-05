package com.wealthfront.magellan

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.init.NavigationOverride
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.navigation.CurrentNavigableProvider
import com.wealthfront.magellan.navigation.ViewTemplateApplier

public abstract class LegacyExpedition<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer,
  navigationOverrides: List<NavigationOverride> = Magellan.getNavigationOverrides(),
  templateApplier: ViewTemplateApplier? = null
) : Step<V>(createBinding) {

  public open var navigator: Navigator by attachFieldToLifecycle(
    Navigator({ viewBinding!!.container() }, navigationOverrides, templateApplier)
  )

  public fun setCurrentNavProvider(currentNavigableProvider: CurrentNavigableProvider) {
    navigator.currentNavigableProvider = currentNavigableProvider
  }
}
