package com.wealthfront.magellan.sample.migration.tide

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.wealthfront.magellan.LegacyStep
import com.wealthfront.magellan.OpenForMocking
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.rx2.RxDisposer
import com.wealthfront.magellan.sample.migration.AppComponentContainer
import com.wealthfront.magellan.sample.migration.api.DogApi
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import javax.inject.Inject

@OpenForMocking
class HelpScreen(private val goToBreedsStep: () -> Unit) : LegacyStep<HelpView>() {

  @Inject lateinit var api: DogApi
  private val rxUnsubscriber by attachFieldToLifecycle(RxDisposer())

  override fun onCreate(context: Context) {
    (context.applicationContext as AppComponentContainer).injector().inject(this)
  }

  override fun createView(context: Context): HelpView {
    return HelpView(context, this)
  }

  override fun onShow(context: Context) {
    rxUnsubscriber.autoDispose(
      api.getRandomImageForBreed("husky")
        .observeOn(mainThread())
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
          goToBreedsStep()
        }
        .create()
    }
  }
}
