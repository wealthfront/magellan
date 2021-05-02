package com.wealthfront.magellan.navigation

import com.wealthfront.magellan.core.Displayable
import com.wealthfront.magellan.lifecycle.LifecycleAware

public interface NavigableCompat<ViewType : Any> : LifecycleAware, Displayable<ViewType>
