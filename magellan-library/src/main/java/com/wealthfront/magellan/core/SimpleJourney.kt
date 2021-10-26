package com.wealthfront.magellan.core

import com.wealthfront.magellan.databinding.MagellanSimpleJourneyBinding

public open class SimpleJourney : Journey<MagellanSimpleJourneyBinding>(
  MagellanSimpleJourneyBinding::inflate,
  MagellanSimpleJourneyBinding::magellanContainer
)
