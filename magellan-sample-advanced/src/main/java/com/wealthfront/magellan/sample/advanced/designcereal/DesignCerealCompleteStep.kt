package com.wealthfront.magellan.sample.advanced.designcereal

import android.content.Context
import com.wealthfront.magellan.sample.advanced.databinding.DesignCerealCompleteBinding
import com.wealthfront.magellan.sample.advanced.designcereal.CerealPieceType.CORN_FLAKE
import com.wealthfront.magellan.sample.advanced.designcereal.CerealPieceType.RINGS
import com.wealthfront.magellan.core.Step

class DesignCerealCompleteStep(
  private val customCereal: CustomCereal,
  private val finishCreation: () -> Unit
) : Step<DesignCerealCompleteBinding>(DesignCerealCompleteBinding::inflate) {

  override fun onShow(context: Context, binding: DesignCerealCompleteBinding) {
    binding.pieceType.text = when (customCereal.pieceType!!) {
      RINGS -> "Rings"
      CORN_FLAKE -> "Corn Flakes"
    }
    binding.pieceStyle.text = when (customCereal.pieceStyle!!) {
      CerealPieceStyle.FROSTED -> "Frosted"
      CerealPieceStyle.PLAIN -> "Plain"
    }
    binding.pieceColor.text = when (customCereal.pieceColor!!) {
      CerealPieceColor.NATURAL -> "Natural"
      CerealPieceColor.RAINBOW -> "Rainbow"
    }
    binding.done.setOnClickListener { finishCreation() }
  }
}