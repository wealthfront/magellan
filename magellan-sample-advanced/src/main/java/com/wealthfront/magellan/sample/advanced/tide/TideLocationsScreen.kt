package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.databinding.HomeBinding

class TideLocationsScreen(private val goToTideDetailsScreen: (noaaApiId: Int, tideLocationName: String) -> Unit) :
  Step<HomeBinding>(HomeBinding::inflate) {

  override fun onShow(context: Context, binding: HomeBinding) {
    binding.tideLocationsGrid.adapter = TideLocationsListAdapter(context)
  }

  fun tideLocationSelected(noaaApiId: Int, tideLocationName: String) {
    goToTideDetailsScreen(noaaApiId, tideLocationName)
  }

  enum class TideLocations(
    val noaaApiId: Int,
    @DrawableRes val imageId: Int,
    val locationName: String
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

  private inner class TideLocationsListAdapter(context: Context) :
    ArrayAdapter<TideLocations?>(
      context, R.layout.tide_location_grid_item, R.id.tideLocationName,
      TideLocations.values()) {

    override fun getView(
      position: Int,
      convertView: View?,
      parent: ViewGroup
    ): View {
      var view = convertView
      if (convertView == null) {
        view = View.inflate(context, R.layout.tide_location_grid_item, null)
      }
      val tideLocation = getItem(position)
      val tideLocationImageView = view!!.findViewById<ImageView>(R.id.tideLocationImage)
      val tideLocationTextView = view.findViewById<TextView>(R.id.tideLocationName)
      tideLocationImageView.setImageResource(tideLocation!!.imageId)
      tideLocationTextView.text = tideLocation.locationName
      view.setOnClickListener {
        tideLocationSelected(tideLocation.noaaApiId, tideLocation.locationName)
      }
      return view
    }
  }
}
