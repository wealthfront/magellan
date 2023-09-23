package com.wealthfront.magellan.sample.migration.tide

import android.content.Context
import android.view.View
import android.widget.Toast
import com.wealthfront.magellan.Screen
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.rx2.RxDisposer
import com.wealthfront.magellan.sample.migration.R
import com.wealthfront.magellan.sample.migration.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.migration.api.DogApi
import com.wealthfront.magellan.sample.migration.toolbar.ToolbarHelper
import com.wealthfront.magellan.transitions.CircularRevealTransition
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import javax.inject.Inject

class DogDetailsScreen(private val breed: String) : Screen<DogDetailsView>() {

  @Inject lateinit var api: DogApi
  private val rxUnsubscriber by attachFieldToLifecycle(RxDisposer())

  override fun createView(context: Context): DogDetailsView {
    app(context).injector().inject(this)
    return DogDetailsView(context)
  }

  override fun onShow(context: Context) {
    ToolbarHelper.setTitle("Dog Breed Info")
    ToolbarHelper.setMenuIcon(R.drawable.clock_white) {
      Toast.makeText(activity, "Menu - Notifications clicked", Toast.LENGTH_SHORT).show()
    }
    ToolbarHelper.setMenuColor(R.color.water)
    rxUnsubscriber.autoDispose(
      api.getRandomImageForBreed(breed)
        .observeOn(mainThread())
        .subscribe {
          view!!.setDogPic(it.message)
        }
    )
  }

  fun goToHelpScreen(originView: View) {
    navigator.goTo(HelpJourney(), CircularRevealTransition(originView))
  }
}
