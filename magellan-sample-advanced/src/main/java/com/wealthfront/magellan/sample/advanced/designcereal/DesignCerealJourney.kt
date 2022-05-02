package com.wealthfront.magellan.sample.advanced.designcereal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.VisibleForTesting
import com.wealthfront.magellan.core.SimpleJourney
import com.wealthfront.magellan.databinding.MagellanSimpleJourneyBinding
import com.wealthfront.magellan.init.Magellan
import com.wealthfront.magellan.lifecycle.attachFieldToLifecycle
import com.wealthfront.magellan.navigation.DefaultLinearNavigator
import com.wealthfront.magellan.navigation.LinearNavigator
import com.wealthfront.magellan.navigation.ViewTemplateApplier
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.ToolbarHelper
import com.wealthfront.magellan.sample.advanced.databinding.DesignCerealTemplateBinding
import javax.inject.Inject

class DesignCerealJourney(private val cerealComplete: () -> Unit) : SimpleJourney() {

  override var navigator: LinearNavigator by attachFieldToLifecycle(
    DefaultLinearNavigator(
      { viewBinding!!.getContainer() },
      Magellan.navigationOverrides,
      object : ViewTemplateApplier {
        override fun onViewCreated(context: Context, view: View): View {
          val inflater = LayoutInflater.from(context)
          val template = DesignCerealTemplateBinding.inflate(inflater).apply {
            this.content.addView(view)
          }
          return template.root
        }
      }
    )
  )

  @Inject lateinit var toolbarHelper: ToolbarHelper
  private var customCereal: CustomCereal? = null

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
    val piecesStep = DesignCerealPiecesStep { pieceType -> onPiecesSelected(pieceType) }
    navigator.goTo(piecesStep)
  }

  override fun onShow(context: Context, binding: MagellanSimpleJourneyBinding) {
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
