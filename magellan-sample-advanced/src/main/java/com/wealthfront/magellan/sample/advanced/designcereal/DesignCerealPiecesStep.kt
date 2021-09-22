package com.wealthfront.magellan.sample.advanced.designcereal

import android.content.Context
import com.wealthfront.magellan.core.Step
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.databinding.DesignCerealPiecesBinding
import com.wealthfront.magellan.sample.advanced.designcereal.CerealPieceType.CORN_FLAKE
import com.wealthfront.magellan.sample.advanced.designcereal.CerealPieceType.RINGS

class DesignCerealPiecesStep(
  private val pieceTypeSelected: (CerealPieceType) -> Unit
) : Step<DesignCerealPiecesBinding>(DesignCerealPiecesBinding::inflate) {

  override fun onShow(context: Context, binding: DesignCerealPiecesBinding) {
    binding.piecesSelection.setOnCheckedChangeListener { group, checkedId ->
      if (checkedId == R.id.cornflakes) {
        pieceTypeSelected(CORN_FLAKE)
      } else if (checkedId == R.id.rings) {
        pieceTypeSelected(RINGS)
      }
    }
  }
}
