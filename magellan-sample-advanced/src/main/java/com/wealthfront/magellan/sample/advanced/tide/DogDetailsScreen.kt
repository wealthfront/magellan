package com.wealthfront.magellan.sample.advanced.tide

import android.content.Context
import android.view.Menu
import android.widget.Toast
import com.wealthfront.magellan.Screen
import com.wealthfront.magellan.lifecycle.lifecycle
import com.wealthfront.magellan.rx.RxUnsubscriber
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.api.DogApi
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

  override fun getActionBarColorRes() = R.color.water

  override fun getTitle(context: Context) = "Dog Breed Info"

  override fun onUpdateMenu(menu: Menu) {
    menu.findItem(R.id.notifications)
      .setVisible(true)
      .setOnMenuItemClickListener {
        Toast.makeText(activity, "Menu - Notifications clicked", Toast.LENGTH_SHORT).show()
        return@setOnMenuItemClickListener true
      }
  }

  override fun onShow(context: Context) {
    rxUnsubscriber.autoUnsubscribe(
      api.getRandomImageForBreed(breed)
        .subscribeOn(Schedulers.io())
        .observeOn(mainThread())
        .subscribe {
          view!!.setDogPic(it.message)
          setTitle(breed)
        }
    )
  }

  fun goToHelpScreen() {
    navigator.goTo(HelpScreen())
  }
}
