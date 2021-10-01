package com.wealthfront.magellan.sample.advanced.ordertickets

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.ToolbarHelper
import com.wealthfront.magellan.sample.advanced.databinding.OrderTicketsSuccessBinding
import javax.inject.Inject

class OrderTicketsSuccessStep(
  private val orderComplete: () -> Unit
) : Step<OrderTicketsSuccessBinding>(OrderTicketsSuccessBinding::inflate) {

  @Inject lateinit var toolbarHelper: ToolbarHelper

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, binding: OrderTicketsSuccessBinding) {
    toolbarHelper.setTitle("Thank you")
    toolbarHelper.showToolbar()
    binding.done.setOnClickListener { orderComplete() }
  }
}
