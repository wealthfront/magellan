package com.wealthfront.magellan

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.navigation.NavigableCompat
import com.wealthfront.magellan.navigation.NavigationOverrideProvider
import com.wealthfront.magellan.navigation.ViewTemplateApplier

public abstract class LegacyJourney<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer,
  navigationOverrides: NavigationOverrideProvider? = Magellan.getNavigationOverrideProvider(),
  templateApplier: ViewTemplateApplier? = null
) : Step<V>(createBinding) {

  public open var navigator: Navigator by attachFieldToLifecycle(
    Navigator({ viewBinding!!.container() }, navigationOverrides, templateApplier)
  )

  override val currentNavigable: NavigableCompat
    get() = navigator.backStack.firstOrNull()?.navigable?.currentNavigable ?: super.currentNavigable
}
