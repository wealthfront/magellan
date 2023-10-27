package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.widget.Toast
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.Screen
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.R
import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

@AssistedFactory
fun interface DogDetailsScreenFactory {
  fun create(breed: String): DogDetailsScreen
}

@OpenForMocking
class DogDetailsScreen @AssistedInject constructor(
  private val api: DogApi,
  private val toolbarHelper: ToolbarHelper,
  @Assisted private val breed: String
) : Screen<DogDetailsView>() {


  override fun createView(context: Context): DogDetailsView {
    (context.applicationContext as AppComponentContainer).injector().inject(this)
    return DogDetailsView(context)
  }

  override fun onShow(context: Context) {
    toolbarHelper.setTitle("Dog Breed Info")
    toolbarHelper.setMenuIcon(R.drawable.clock_white) {
      Toast.makeText(activity, "Menu - Notifications clicked", Toast.LENGTH_SHORT).show()
    }
    toolbarHelper.setMenuColor(R.color.water)

    shownScope.launch {
      val imageResponse = runCatching { api.getRandomImageForBreed(breed) }
      imageResponse.onSuccess { image ->
        view!!.setDogPic(image.message)
      }
      imageResponse.onFailure { throwable ->
        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
      }
    }
  }
}
