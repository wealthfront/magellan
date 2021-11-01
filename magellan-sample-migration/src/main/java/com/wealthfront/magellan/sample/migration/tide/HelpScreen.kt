package com.wealthfront.magellan.sample.migration.tide

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.wealthfront.magellan.LegacyStep
import com.wealthfront.magellan.Navigator
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.rx.RxUnsubscriber
import com.wealthfront.magellan.sample.migration.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.migration.api.DogApi
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class HelpScreen(navigator: Navigator) : LegacyStep<HelpView>(navigator) {

  @Inject lateinit var api: DogApi
  private val rxUnsubscriber by attachFieldToLifecycle(RxUnsubscriber())

  override fun createView(context: Context): HelpView {
    app(context).injector().inject(this)
    return HelpView(context, this)
  }

  override fun onShow(context: Context) {
    super.onShow(context)
    rxUnsubscriber.autoUnsubscribe(
      api.getRandomImageForBreed("husky")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          view!!.setDogPic(it.message)
        }
    )
  }

  fun showHelpDialog() {
    dialogComponent.showDialog { context ->
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
