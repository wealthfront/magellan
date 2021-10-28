package com.wealthfront.magellan.internal.test

import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.internal.test.databinding.MagellanDummyLayoutBinding

public open class DummyStep : Step<MagellanDummyLayoutBinding>(MagellanDummyLayoutBinding::inflate)
