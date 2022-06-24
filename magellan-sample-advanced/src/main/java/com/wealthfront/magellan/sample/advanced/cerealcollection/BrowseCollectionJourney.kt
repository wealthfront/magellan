package com.wealthfront.magellan.sample.advanced.cerealcollection

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import com.google.android.material.button.MaterialButton
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.rx2.RxDisposer
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.ToolbarHelper
import com.wealthfront.magellan.sample.advanced.api.ApiClient
import com.wealthfront.magellan.sample.advanced.databinding.BrowseCollectionBinding
import com.wealthfront.magellan.transitions.CrossfadeTransition
import com.wealthfront.magellan.transitions.NoAnimationTransition
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import javax.inject.Inject

class BrowseCollectionJourney : Journey<BrowseCollectionBinding>(
  BrowseCollectionBinding::inflate,
  BrowseCollectionBinding::cerealDetailContainer
) {

  private val rxUnsubscriber by attachFieldToLifecycle(RxDisposer())
  @Inject lateinit var apiClient: ApiClient
  @Inject lateinit var toolbarHelper: ToolbarHelper

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, binding: BrowseCollectionBinding) {
    toolbarHelper.hideToolbar()
    rxUnsubscriber.autoDispose(
      apiClient.getCollection()
        .observeOn(mainThread())
        .doOnSubscribe { binding.loadingLayout.showLoading() }
        .doOnTerminate { binding.loadingLayout.hideLoading() }
        .subscribe { cereals ->
          cereals.forEachIndexed { index, cerealDetails ->
            val cerealButton = createCerealButton(context, cerealDetails)
            binding.cerealButtonContainer.addView(cerealButton)

            if (index == 0) {
              cerealButton.isEnabled = false
              navigator.goTo(CerealDetailStep(cereals[0]), NoAnimationTransition())
            }
          }
        }
    )
  }

  private fun createCerealButton(context: Context, cerealDetails: CerealDetails): AppCompatButton {
    return MaterialButton(context).apply {
      setText(cerealDetails.title)
      setOnClickListener {
        enableMenuChoices()
        it.isEnabled = false
        navigator.replace(CerealDetailStep(cerealDetails), CrossfadeTransition())
      }
    }
  }

  private fun enableMenuChoices() {
    this.viewBinding!!.cerealButtonContainer.children.forEach {
      it.isEnabled = true
    }
  }
}
