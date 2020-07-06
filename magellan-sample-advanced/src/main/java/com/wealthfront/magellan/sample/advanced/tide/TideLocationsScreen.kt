package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.wealthfront.magellan.core.Screen
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.model.TideLocations

class TideLocationsScreen(private val onLocationSelected: (Int) -> Unit) : Screen(R.layout.home) {

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, view: View) {
    val tideLocationsList = view.findViewById<GridView>(R.id.tideLocationsGrid)
    tideLocationsList.adapter = TideLocationsListAdapter(context)
  }

  fun tideLocationSelected(noaaApiId: Int) {
    onLocationSelected(noaaApiId)
  }

  private inner class TideLocationsListAdapter(context: Context) : ArrayAdapter<TideLocations>(
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
        tideLocationSelected(tideLocation.noaaApiId)
      }
      return view
    }
  }
}