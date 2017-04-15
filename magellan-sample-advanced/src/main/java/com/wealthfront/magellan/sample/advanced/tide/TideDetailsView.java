package com.wealthfront.magellan.sample.advanced.tide;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wealthfront.magellan.BaseScreenView;
import com.wealthfront.magellan.sample.advanced.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;

class TideDetailsView extends BaseScreenView<TideDetailsScreen> {

  @BindView(R.id.loading) ProgressBar loading;
  @BindView(R.id.tideDetailsContent) View content;
  @BindView(R.id.currentWaterLevel) TextView currentWaterLevel;
  @BindView(R.id.highestWaterLevel) TextView highestWaterLevel;
  @BindView(R.id.lowestWaterLevel) TextView lowestWaterLevel;
  @BindView(R.id.currentWaterLevelBottomSpacing) View currentWaterLevelBottomSpacing;
  @BindView(R.id.currentWaterLevelTopSpacing) View currentWaterLevelTopSpacing;

  TideDetailsView(Context context) {
    super(context);
    inflate(context, R.layout.tide_detail, this);
    bind(this);
  }

  public void setTideHeights(
      BigDecimal latestMeasuredTideHeight, BigDecimal lowestMeasuredTideHeight,
      BigDecimal highestMeasuredTideHeight) {
    showContent();
    displayWaterLevelText(latestMeasuredTideHeight, lowestMeasuredTideHeight,
        highestMeasuredTideHeight);
    setVisibleWaterLevel(latestMeasuredTideHeight, lowestMeasuredTideHeight,
        highestMeasuredTideHeight);
  }

  private void showContent() {
    content.setAlpha(0f);
    content.setVisibility(VISIBLE);
    loading.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        content.animate().alpha(1f).start();
        loading.setVisibility(GONE);
      }
    }).start();
  }

  private void displayWaterLevelText(
      BigDecimal latestMeasuredTideHeight, BigDecimal lowestMeasuredTideHeight,
      BigDecimal highestMeasuredTideHeight) {
    currentWaterLevel.setText(String.format(Locale.US, "Current Water Level: %.2f ft",
        latestMeasuredTideHeight.setScale(2, RoundingMode.HALF_UP)));
    highestWaterLevel.setText(String.format(Locale.US, "Today's Highest Water Level: %.2f ft",
        highestMeasuredTideHeight.setScale(2, RoundingMode.HALF_UP)));
    lowestWaterLevel.setText(String.format(Locale.US, "Today's Lowest Water Level: %.2f ft",
        lowestMeasuredTideHeight.setScale(2, RoundingMode.HALF_UP)));
  }

  private void setVisibleWaterLevel(
      BigDecimal latestMeasuredTideHeight, BigDecimal lowestMeasuredTideHeight,
      BigDecimal highestMeasuredTideHeight) {
    float percentOfMax = latestMeasuredTideHeight.subtract(lowestMeasuredTideHeight)
        .divide(highestMeasuredTideHeight.subtract(lowestMeasuredTideHeight), RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).floatValue();
    currentWaterLevelBottomSpacing.setLayoutParams(new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, 2, percentOfMax));
    currentWaterLevelTopSpacing.setLayoutParams(new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, 2, 100 - percentOfMax));
  }

}
