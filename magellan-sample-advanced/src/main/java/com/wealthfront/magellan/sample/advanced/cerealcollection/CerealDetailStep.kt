package com.wealthfront.magellan.sample.advanced.cerealcollection

import android.content.Context
import androidx.annotation.StringRes
import com.wealthfront.magellan.sample.advanced.databinding.CerealDetailBinding
import com.wealthfront.magellan.core.Step

class CerealDetailStep(private val cerealDetails: CerealDetails) :
  Step<CerealDetailBinding>(CerealDetailBinding::inflate) {

  override fun onShow(context: Context, binding: CerealDetailBinding) {
    binding.cerealTitle.text = context.getString(cerealDetails.title)
    binding.cerealDescription.text = context.getString(cerealDetails.description)
    binding.cerealStatus.text = when (cerealDetails.status) {
      CerealStatus.ACTIVE -> "Active"
      CerealStatus.DISCONTINUED -> "Discontinued"
      CerealStatus.LIMITED -> "Limited Availability"
    }
  }
}

data class CerealDetails(
  @StringRes val title: Int,
  @StringRes val description: Int,
  val status: CerealStatus
)

enum class CerealStatus {
  ACTIVE,
  DISCONTINUED,
  LIMITED
}