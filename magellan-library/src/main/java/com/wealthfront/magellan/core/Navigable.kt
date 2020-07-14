package com.wealthfront.magellan.core

import com.wealthfront.magellan.lifecycle.LifecycleAware
import com.wealthfront.magellan.view.Displayable

interface Navigable : LifecycleAware, Displayable {

  var nextNavigable: Navigable?
}