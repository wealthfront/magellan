package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import com.wealthfront.magellan.compose.core.Screen
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.databinding.HomeBinding
import com.wealthfront.magellan.sample.advanced.model.TideLocations

class TideLocationsScreen(private val onLocationSelected: (Int) -> Unit) : Screen<HomeBinding>(HomeBinding::inflate) {

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, binding: HomeBinding) {
    binding.tideLocationsGrid.adapter = TideLocationsListAdapter(context, onLocationSelected)
  }
}