package com.wealthfront.magellan.sample.advanced.designcereal

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.databinding.DesignCerealStartBinding

class DesignCerealStartStep(private val goToDesignCereal: () -> Unit) :
  Step<DesignCerealStartBinding>(DesignCerealStartBinding::inflate) {

  override fun onShow(context: Context, binding: DesignCerealStartBinding) {
    binding.designCerealStart.setOnClickListener { goToDesignCereal() }
  }
}
