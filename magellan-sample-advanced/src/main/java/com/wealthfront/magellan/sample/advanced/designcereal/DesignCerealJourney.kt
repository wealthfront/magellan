package com.wealthfront.magellan.sample.advanced.designcereal

import android.content.Context
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.advanced.ToolbarHelperProvider
import com.wealthfront.magellan.sample.advanced.databinding.DesignCerealBinding

class DesignCerealJourney(private val cerealComplete: () -> Unit) : Journey<DesignCerealBinding>(
  DesignCerealBinding::inflate,
  DesignCerealBinding::container
) {

  private var customCereal: CustomCereal? = null
  override fun onCreate(context: Context) {
    val piecesStep = DesignCerealPiecesStep { pieceType ->
      customCereal = CustomCereal(pieceType = pieceType)
      piecesSelected()
    }
    navigator.goTo(piecesStep)
  }

  override fun onShow(context: Context, binding: DesignCerealBinding) {
    ToolbarHelperProvider.toolbarHelper.hideToolbar()
  }

  private fun piecesSelected() {
    val styleStep = DesignCerealStyleStep { pieceStyle, pieceColor ->
      customCereal = customCereal!!.copy(pieceStyle = pieceStyle, pieceColor = pieceColor)
      styleSelected()
    }
    navigator.goTo(styleStep)
  }

  private fun styleSelected() {
    val completeStep = DesignCerealCompleteStep(customCereal!!, cerealComplete)
    navigator.goTo(completeStep)
  }
}

enum class CerealPieceType {
  CORN_FLAKE,
  RINGS
}

enum class CerealPieceStyle {
  PLAIN,
  FROSTED
}

enum class CerealPieceColor {
  NATURAL,
  RAINBOW
}

data class CustomCereal(
  var pieceType: CerealPieceType? = null,
  var pieceStyle: CerealPieceStyle? = null,
  var pieceColor: CerealPieceColor? = null
)
