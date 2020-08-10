package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import com.wealthfront.magellan.Screen
import com.wealthfront.magellan.sample.advanced.DogApi
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import javax.inject.Inject
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers

class DogDetailsScreen(private val breed: String) : Screen<DogDetailsView>() {

  @Inject lateinit var api: DogApi

  override fun createView(context: Context): DogDetailsView {
    app(context).injector().inject(this)
    return DogDetailsView(context)
  }

  override fun onShow(context: Context) {
    api.getRandomImageForBreed(breed)
      .subscribeOn(Schedulers.io())
      .observeOn(mainThread())
      .subscribe {
        view!!.setDogPic(it.message)
      }
  }

  fun goToHelpScreen() {
    navigator.goTo(HelpScreen())
  }
}
