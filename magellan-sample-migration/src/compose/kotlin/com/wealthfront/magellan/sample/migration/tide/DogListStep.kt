package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.runtime.mutableStateListOf
import com.wealthfront.magellan.coroutines.ShownLifecycleScope
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.api.DogApi
import kotlinx.coroutines.launch
import javax.inject.Inject

class DogBreedsStep : ComposeStep() {

  @Inject lateinit var api: DogApi

  private val scope by attachFieldToLifecycle(ShownLifecycleScope())
  private val dogBreedsData = mutableStateListOf<String>()

  override fun onCreate(context: Context) {
    (context.applicationContext as AppComponentContainer).injector().inject(this)
  }

  override fun onShow(context: Context) {
    setContent {
      DogBreeds(dogBreeds = dogBreedsData)
    }

    scope.launch {
      // show loading
      val breeds = runCatching { api.getListOfAllBreedsOfRetriever() }
      breeds.onSuccess { response ->
        dogBreedsData.clear()
        dogBreedsData.addAll(response.message)
      }.onFailure {
        Toast.makeText(context, it.message, LENGTH_SHORT).show()
      }
      // hide loading
    }
  }

}