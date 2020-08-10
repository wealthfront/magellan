package com.wealthfront.magellan.sample.advanced.tide

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.wealthfront.magellan.Screen
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.rx.RxUnsubscriber
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.api.DogApi
import javax.inject.Inject
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class HelpScreen : Screen<HelpView>() {

  @Inject lateinit var api: DogApi
  private val rxUnsubscriber by lifecycle(RxUnsubscriber())

  override fun createView(context: Context): HelpView {
    app(context).injector().inject(this)
    return HelpView(context)
  }

  override fun onShow(context: Context) {
    super.onShow(context)
    rxUnsubscriber.autoUnsubscribe(api.getRandomImageForBreed("husky")
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        view!!.setDogPic(it.message)
      })
  }

  fun showHelpDialog() {
    showDialog { context ->
      AlertDialog.Builder(context)
        .setTitle("Hello")
        .setMessage("Did you find the dog you were looking for?")
        .setPositiveButton("Find all breeds of retriever") { _: DialogInterface, _: Int ->
          navigator.show(DogBreedsStep())
        }
        .create()
    }
  }
}
