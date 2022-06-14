package com.wealthfront.magellan

import com.wealthfront.magellan.databinding.MagellanSimpleJourneyBinding

public open class SimpleLegacyJourney : LegacyJourney<MagellanSimpleJourneyBinding>(
  MagellanSimpleJourneyBinding::inflate,
  MagellanSimpleJourneyBinding::magellanContainer
)