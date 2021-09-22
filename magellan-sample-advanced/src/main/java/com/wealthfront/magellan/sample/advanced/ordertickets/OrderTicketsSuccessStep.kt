package com.wealthfront.magellan.sample.advanced.ordertickets

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.ToolbarHelperProvider
import com.wealthfront.magellan.sample.advanced.databinding.OrderTicketsSuccessBinding

class OrderTicketsSuccessStep(
  private val orderComplete: () -> Unit
) : Step<OrderTicketsSuccessBinding>(OrderTicketsSuccessBinding::inflate) {

  override fun onShow(context: Context, binding: OrderTicketsSuccessBinding) {
    ToolbarHelperProvider.toolbarHelper.setTitle("Thank you")
    ToolbarHelperProvider.toolbarHelper.showToolbar()
    binding.done.setOnClickListener { orderComplete() }
  }
}
