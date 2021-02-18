package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.coroutines.NavigableScope
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.api.DogApi
import com.wealthfront.magellan.sample.advanced.databinding.DogBreedBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class DogBreedsStep : Step<DogBreedBinding>(DogBreedBinding::inflate) {

  @Inject lateinit var api: DogApi

  private val scope by lifecycle(NavigableScope())

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, binding: DogBreedBinding) {
    scope.launch {
      // show loading
      val breeds = runCatching { api.getListOfAllBreedsOfRetriever() }
      breeds.onSuccess {
        binding.dogBreeds.adapter = DogBreedListAdapter(context, it.message)
      }.onFailure {
        Toast.makeText(context, it.message, LENGTH_SHORT).show()
      }
      // hide loading
    }
  }

  private inner class DogBreedListAdapter(context: Context, breeds: List<String>) :
    ArrayAdapter<String>(context, R.layout.dog_item, R.id.dogName, breeds) {

    override fun getView(
      position: Int,
      convertView: View?,
      parent: ViewGroup
    ): View {
      var view = convertView
      if (convertView == null) {
        view = View.inflate(context, R.layout.dog_item, null)
      }
      val dogDetail = getItem(position)!!
      val dogDetailTextView = view!!.findViewById<TextView>(R.id.dogName)
      dogDetailTextView.text = dogDetail
      return view
    }
  }
}
