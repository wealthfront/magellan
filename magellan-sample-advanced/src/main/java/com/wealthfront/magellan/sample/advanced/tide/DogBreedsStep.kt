package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.DogApi
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.databinding.DogBreedBinding
import javax.inject.Inject
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class DogBreedsStep : Step<DogBreedBinding>(DogBreedBinding::inflate) {

  @Inject lateinit var api: DogApi

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, binding: DogBreedBinding) {
    api.getListOfAllBreedsOfRetriever()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { breeds ->
        binding.dogBreeds.adapter = DogBreedListAdapter(context, breeds.message)
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
