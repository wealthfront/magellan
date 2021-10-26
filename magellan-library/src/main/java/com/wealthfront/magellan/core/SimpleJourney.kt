package com.wealthfront.magellan.core

import com.wealthfront.magellan.databinding.MagellanRootBinding

public open class SimpleJourney : Journey<MagellanRootBinding>(
  MagellanRootBinding::inflate,
  MagellanRootBinding::magellanContainer
)