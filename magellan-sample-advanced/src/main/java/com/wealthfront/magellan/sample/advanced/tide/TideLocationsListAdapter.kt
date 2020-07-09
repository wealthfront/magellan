package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.model.TideLocations

internal class TideLocationsListAdapter(context: Context, private val onLocationSelected: (Int) -> Unit) : ArrayAdapter<TideLocations>(
  context,
  R.layout.tide_location_grid_item,
  R.id.tideLocationName,
  TideLocations.values()
) {

  override fun getView(
    position: Int,
    convertView: View?,
    parent: ViewGroup
  ): View {
    val view = convertView ?: View.inflate(context, R.layout.tide_location_grid_item, null)
    val tideLocation = getItem(position)
    val tideLocationImageView = view.findViewById<ImageView>(R.id.tideLocationImage)
    val tideLocationTextView = view.findViewById<TextView>(R.id.tideLocationName)
    tideLocationImageView?.setImageResource(tideLocation!!.imageId)
    tideLocationTextView?.text = tideLocation!!.locationName
    view.setOnClickListener {
      onLocationSelected(tideLocation.noaaApiId)
    }
    return view
  }
}