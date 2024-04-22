package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.runtime.mutableStateListOf
import com.wealthfront.magellan.coroutines.ShownLifecycleScope
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.sample.migration.api.DogApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

@AssistedFactory
fun interface DogListStepFactory {
  fun create(goToDogDetails: (name: String) -> Unit): DogListStep
}

class DogListStep @AssistedInject constructor(
  private val api: DogApi,
  @Assisted private val goToDogDetails: (name: String) -> Unit
) : ComposeStep() {

  private val scope by attachFieldToLifecycle(ShownLifecycleScope())
  private val dogBreedsData = mutableStateListOf<String>()

  override fun onShow(context: Context) {
    setContent {
      DogBreeds(dogBreeds = dogBreedsData)
    }

    scope.launch {
      // show loading
      val breeds = runCatching { api.getAllBreeds() }
      breeds.onSuccess { response ->
        dogBreedsData.clear()
        dogBreedsData.addAll(response.message.keys)
      }.onFailure {
        Toast.makeText(context, it.message, LENGTH_SHORT).show()
      }
      // hide loading
    }
  }
}
