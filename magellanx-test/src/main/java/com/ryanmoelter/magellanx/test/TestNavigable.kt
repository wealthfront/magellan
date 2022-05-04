package com.ryanmoelter.magellanx.test

import androidx.compose.runtime.Composable
import com.ryanmoelter.magellanx.core.Navigable
import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAwareComponent
import com.ryanmoelter.magellanx.core.lifecycle.createAndAttachFieldToLifecycleWhenShown

/**
 * A simple [Navigable] implementation for use in tests.
 */
public class TestNavigable : Navigable<@Composable () -> Unit>, LifecycleAwareComponent() {

  public override val view: (@Composable () -> Unit)? by createAndAttachFieldToLifecycleWhenShown {
    { }
  }
}
