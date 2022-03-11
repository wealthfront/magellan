package com.wealthfront.magellan.internal.test

import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.internal.test.databinding.InstrumentedBinding

public class InstrumentedStep : Step<InstrumentedBinding>(InstrumentedBinding::inflate)