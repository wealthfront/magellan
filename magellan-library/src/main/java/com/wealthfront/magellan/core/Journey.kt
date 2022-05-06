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
import com.wealthfront.magellan.navigation.NavigationOverrideProvider
import com.wealthfront.magellan.navigation.ViewTemplateApplier

public abstract class Journey<V : ViewBinding>(
  inflateBinding: (LayoutInflater) -> V,
  protected val getContainer: V.() -> ScreenContainer,
  navigationOverrideProvider: NavigationOverrideProvider? = Magellan.getNavigationOverrideProvider(),
  templateApplier: ViewTemplateApplier? = null
) : Step<V>(inflateBinding) {

  @VisibleForTesting(otherwise = PROTECTED)
  public open var navigator: LinearNavigator by attachFieldToLifecycle(
    DefaultLinearNavigator({ viewBinding!!.getContainer() }, navigationOverrideProvider, templateApplier)
  )
}
