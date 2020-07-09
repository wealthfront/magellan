package com.wealthfront.magellan.sample.advanced.tide

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import com.wealthfront.magellan.compose.core.Screen
import com.wealthfront.magellan.sample.advanced.NoaaApi
import com.wealthfront.magellan.sample.advanced.R
import com.wealthfront.magellan.sample.advanced.SampleApplication.Companion.app
import com.wealthfront.magellan.sample.advanced.databinding.TideDetailBinding
import com.wealthfront.magellan.sample.advanced.model.Observation
import rx.android.schedulers.AndroidSchedulers
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.ArrayList
import java.util.Collections
import java.util.Comparator
import java.util.Locale
import javax.inject.Inject

class TideDetailsScreen(var noaaApiId: Int) : Screen<TideDetailBinding>(TideDetailBinding::inflate) {

  @Inject lateinit var noaaApi: NoaaApi

  override fun onCreate(context: Context) {
    app(context).injector().inject(this)
  }

  override fun onShow(context: Context, binding: TideDetailBinding) {
    noaaApi.getTideInfo(noaaApiId)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ tideInfo ->
        if (tideInfo.data != null && tideInfo.data.isNotEmpty()) {
          val observations = tideInfo.data
          val highestMeasuredTideHeight = Collections.max(
            filterOutNullMeasurements(observations),
            OBSERVATION_COMPARATOR)
            .verifiedWaterLevel
          val lowestMeasuredTideHeight = Collections.min(
            filterOutNullMeasurements(observations),
            OBSERVATION_COMPARATOR)
            .verifiedWaterLevel
          val latestMeasuredTideHeight =
            observations[observations.size - 1].verifiedWaterLevel
          setTideHeights(
            binding,
            latestMeasuredTideHeight, lowestMeasuredTideHeight,
            highestMeasuredTideHeight)
        }
      }, {
        Toast.makeText(context, R.string.cannot_retrieve_tide_info, Toast.LENGTH_SHORT).show()
      })
  }

  @VisibleForTesting
  fun setTideHeights(
    binding: TideDetailBinding,
    latestMeasuredTideHeight: BigDecimal, lowestMeasuredTideHeight: BigDecimal,
    highestMeasuredTideHeight: BigDecimal
  ) {
    showContent(binding)
    displayWaterLevelText(
      binding,
      latestMeasuredTideHeight, lowestMeasuredTideHeight,
      highestMeasuredTideHeight)
    setVisibleWaterLevel(
      binding,
      latestMeasuredTideHeight, lowestMeasuredTideHeight,
      highestMeasuredTideHeight)
  }

  private fun showContent(binding: TideDetailBinding) {
    binding.tideDetailsContent.alpha = 0f
    binding.tideDetailsContent.visibility = View.VISIBLE
    binding.loading.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        binding.tideDetailsContent.animate().alpha(1f).start()
        binding.loading.visibility = View.GONE
      }
    }).start()
  }

  private fun displayWaterLevelText(
    binding: TideDetailBinding,
    latestMeasuredTideHeight: BigDecimal, lowestMeasuredTideHeight: BigDecimal,
    highestMeasuredTideHeight: BigDecimal
  ) {
    binding.currentWaterLevel.text = String.format(
      Locale.US, "Current Water Level: %.2f ft",
      latestMeasuredTideHeight.setScale(2, RoundingMode.HALF_UP))
    binding.highestWaterLevel.text = String.format(
      Locale.US, "Today's Highest Water Level: %.2f ft",
      highestMeasuredTideHeight.setScale(2, RoundingMode.HALF_UP))
    binding.lowestWaterLevel.text = String.format(
      Locale.US, "Today's Lowest Water Level: %.2f ft",
      lowestMeasuredTideHeight.setScale(2, RoundingMode.HALF_UP))
  }

  private fun setVisibleWaterLevel(
    binding: TideDetailBinding,
    latestMeasuredTideHeight: BigDecimal, lowestMeasuredTideHeight: BigDecimal,
    highestMeasuredTideHeight: BigDecimal
  ) {
    val percentOfMax: Float = latestMeasuredTideHeight.subtract(lowestMeasuredTideHeight)
      .divide(
        highestMeasuredTideHeight.subtract(lowestMeasuredTideHeight),
        RoundingMode.HALF_UP)
      .multiply(BigDecimal.valueOf(100))
      .setScale(0, BigDecimal.ROUND_HALF_UP)
      .toFloat()
    binding.currentWaterLevelBottomSpacing.layoutParams = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, 2, percentOfMax)
    binding.currentWaterLevelTopSpacing.layoutParams = LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT, 2, 100 - percentOfMax)
  }

  companion object {
    val OBSERVATION_COMPARATOR =
      Comparator<Observation> { o1, o2 -> o1.verifiedWaterLevel.compareTo(o2.verifiedWaterLevel) }

    private fun filterOutNullMeasurements(listWithNulls: List<Observation>): List<Observation> {
      val result: MutableList<Observation> = ArrayList()
      for (item in listWithNulls) {
        if (item.verifiedWaterLevel != null) {
          result.add(item)
        }
      }
      return result
    }
  }
}