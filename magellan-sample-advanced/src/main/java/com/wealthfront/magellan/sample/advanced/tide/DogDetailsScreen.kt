package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.widget.Toast
import com.wealthfront.magellan.Screen
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.rx.RxUnsubscriber
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.api.DogApi
import com.wealthfront.magellan.sample.advanced.toolbar.ToolbarHelper
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers
import javax.inject.Inject

class DogDetailsScreen(private val breed: String) : Screen<DogDetailsView>() {

  @Inject lateinit var api: DogApi
  private val rxUnsubscriber by lifecycle(RxUnsubscriber())

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
    rxUnsubscriber.autoUnsubscribe(
      api.getRandomImageForBreed(breed)
        .subscribeOn(Schedulers.io())
        .observeOn(mainThread())
        .subscribe {
          view!!.setDogPic(it.message)
        }
    )
  }

  fun goToHelpScreen() {
    navigator.goTo(HelpScreen())
  }
}
