package com.wealthfront.magellan.sample.advanced.designcereal

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.wealthfront.magellan.core.Journey
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.ToolbarHelper
import com.wealthfront.magellan.sample.advanced.databinding.DesignCerealBinding
import javax.inject.Inject

class DesignCerealJourney(private val cerealComplete: () -> Unit) : Journey<DesignCerealBinding>(
  DesignCerealBinding::inflate,
  DesignCerealBinding::container
) {

  @Inject lateinit var toolbarHelper: ToolbarHelper
  private var customCereal: CustomCereal? = null

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
    val piecesStep = DesignCerealPiecesStep { pieceType -> onPiecesSelected(pieceType) }
    navigator.goTo(piecesStep)
  }

  override fun onShow(context: Context, binding: DesignCerealBinding) {
    toolbarHelper.hideToolbar()
  }

  @VisibleForTesting
  fun onPiecesSelected(pieceType: CerealPieceType) {
    customCereal = CustomCereal(pieceType = pieceType)
    val styleStep = DesignCerealStyleStep { pieceStyle, pieceColor ->
      onStyleSelected(pieceStyle, pieceColor)
    }
    navigator.goTo(styleStep)
  }

  @VisibleForTesting
  fun onStyleSelected(pieceStyle: CerealPieceStyle, pieceColor: CerealPieceColor) {
    customCereal = customCereal!!.copy(pieceStyle = pieceStyle, pieceColor = pieceColor)
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
