package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import com.wealthfront.magellan.Screen
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.rx.RxUnsubscriber
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.api.DogApi
import javax.inject.Inject
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers

class DogDetailsScreen(private val breed: String) : Screen<DogDetailsView>() {

  @Inject lateinit var api: DogApi
  private val rxUnsubscriber by lifecycle(RxUnsubscriber())

  override fun createView(context: Context): DogDetailsView {
    app(context).injector().inject(this)
    return DogDetailsView(context)
  }

  override fun onShow(context: Context) {
    rxUnsubscriber.autoUnsubscribe(api.getRandomImageForBreed(breed)
      .subscribeOn(Schedulers.io())
      .observeOn(mainThread())
      .subscribe {
        view!!.setDogPic(it.message)
      })
  }

  fun goToHelpScreen() {
    navigator.goTo(HelpScreen())
  }
}
