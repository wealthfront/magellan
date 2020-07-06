package com.wealthfront.magellan.sample.advanced.model

import androidx.annotation.DrawableRes
import com.wealthfront.magellan.sample.advanced.R

enum class TideLocations(
  var noaaApiId: Int,
  @field:DrawableRes @get:DrawableRes @param:DrawableRes var imageId: Int,
  var locationName: String
) {

  SAN_FRANCISCO(9414290, R.drawable.san_francisco, "San Francisco"),
  SEATTLE(9447130, R.drawable.seattle, "Seattle"),
  ANCHORAGE(9455920, R.drawable.anchorage, "Anchorage"),
  GALVESTON(8771450, R.drawable.galveston, "Galveston"),
  MIAMI(8723214, R.drawable.miami, "Miami"),
  CHARLESTON(8665530, R.drawable.charleston, "Charleston"),
  CHESAPEAKE_BAY(8638863, R.drawable.chesapeake, "Chesapeake Bay"),
  NEW_YORK(8518750, R.drawable.new_york, "New York"),
  BOSTON(8443970, R.drawable.boston, "Boston"),
  WASHINGTON_DC(8594900, R.drawable.washington_dc, "Washington D.C.");
}