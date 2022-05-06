package com.wealthfront.magellan

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.navigation.CurrentNavigableProvider
import com.wealthfront.magellan.navigation.NavigationOverrideProvider
import com.wealthfront.magellan.navigation.ViewTemplateApplier

public abstract class LegacyExpedition<V : ViewBinding>(
  createBinding: (LayoutInflater) -> V,
  container: V.() -> ScreenContainer,
  navigationOverrides: NavigationOverrideProvider? = Magellan.getNavigationOverrideProvider(),
  templateApplier: ViewTemplateApplier? = null
) : LegacyJourney<V>(createBinding, container, navigationOverrides, templateApplier) {

  public fun setCurrentNavProvider(currentNavigableProvider: CurrentNavigableProvider) {
    navigator.currentNavigableProvider = currentNavigableProvider
  }
}
