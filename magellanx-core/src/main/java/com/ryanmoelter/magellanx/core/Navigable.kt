package com.ryanmoelter.magellanx.core

import com.ryanmoelter.magellanx.core.lifecycle.LifecycleAware

public interface Navigable<ViewType : Any> : LifecycleAware, Displayable<ViewType>
