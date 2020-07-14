package com.wealthfront.magellan.core

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.wealthfront.magellan.ScreenContainer
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.navigation.Navigator

abstract class Flow<V: ViewBinding>(
  createBinding: (LayoutInflater) -> V
) : Navigable, LifecycleAwareComponent() {

  var navigator by lifecycle(Navigator { view as ScreenContainer })

}