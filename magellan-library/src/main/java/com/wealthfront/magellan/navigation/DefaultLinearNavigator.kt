package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.Direction
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.init.NavigationOverride
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.transitions.MagellanTransition
import java.util.Deque

@OpenForMocking
public class DefaultLinearNavigator constructor(
  container: () -> ScreenContainer,
  navigationOverrides: List<NavigationOverride> = Magellan.getNavigationOverrides(),
  templateApplier: ViewTemplateApplier? = null
) : LinearNavigator, LifecycleAwareComponent() {

  private val delegate by attachFieldToLifecycle(NavigationDelegate(container, navigationOverrides, templateApplier))

  override val backStack: List<NavigationEvent>
    get() = delegate.backStack.toList()

  public override fun goTo(
    navigable: Navigable,
    overrideMagellanTransition: MagellanTransition?
  ) {
    delegate.goTo(navigable, overrideMagellanTransition)
  }

  public override fun replace(
    navigable: Navigable,
    overrideMagellanTransition: MagellanTransition?
  ) {
    delegate.replace(navigable, overrideMagellanTransition)
  }

  public override fun navigate(
    direction: Direction,
    backStackOperation: (Deque<NavigationEvent>) -> MagellanTransition
  ) {
    delegate.navigate(direction, backStackOperation)
  }

  public override fun goBack(): Boolean = delegate.goBack()
}
