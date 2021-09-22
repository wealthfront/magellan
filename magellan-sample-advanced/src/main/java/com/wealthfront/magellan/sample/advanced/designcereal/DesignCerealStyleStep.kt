package com.wealthfront.magellan.sample.advanced.designcereal

import android.content.Context
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.databinding.DesignCerealStyleBinding
import com.wealthfront.magellan.sample.advanced.designcereal.CerealPieceColor.NATURAL
import com.wealthfront.magellan.sample.advanced.designcereal.CerealPieceColor.RAINBOW
import com.wealthfront.magellan.sample.advanced.designcereal.CerealPieceStyle.FROSTED
import com.wealthfront.magellan.sample.advanced.designcereal.CerealPieceStyle.PLAIN
import com.wealthfront.magellan.core.Step

class DesignCerealStyleStep(
  private val goToConfirmation: (CerealPieceStyle, CerealPieceColor) -> Unit
) : Step<DesignCerealStyleBinding>(DesignCerealStyleBinding::inflate) {

  private var pieceStyle: CerealPieceStyle? = null
  private var pieceColor: CerealPieceColor? = null

  override fun onShow(context: Context, binding: DesignCerealStyleBinding) {
    binding.styleSelection.setOnCheckedChangeListener { _, checkedId ->
      if (checkedId == R.id.plain) {
        pieceStyle = PLAIN
      } else if (checkedId == R.id.frosted) {
        pieceStyle = FROSTED
      }
      updateNextButton(binding)
    }

    binding.colorSelection.setOnCheckedChangeListener { _, checkedId ->
      if (checkedId == R.id.natural) {
        pieceColor = NATURAL
      } else if (checkedId == R.id.rainbow) {
        pieceColor = RAINBOW
      }
      updateNextButton(binding)
    }

    binding.next.setOnClickListener {
      goToConfirmation(pieceStyle!!, pieceColor!!)
    }
  }

  private fun updateNextButton(binding: DesignCerealStyleBinding) {
    binding.next.isEnabled = (pieceStyle != null && pieceColor != null)
  }
}