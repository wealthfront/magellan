package com.wealthfront.magellan.test

import android.view.View
import com.wealthfront.magellan.core.Navigable
import com.wealthfront.magellan.lifecycle.LifecycleAwareComponent
import com.wealthfront.magellan.lifecycle.createAndAttachFieldToLifecycleWhenShown

/**
 * A simple [Navigable] implementation for use in tests.
 */
public class TestNavigable : Navigable, LifecycleAwareComponent() {

  public override val view: View? by createAndAttachFieldToLifecycleWhenShown { context -> View(context) }
}