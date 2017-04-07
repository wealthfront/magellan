package com.wealthfront.magellan.sample.advanced;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wealthfront.magellan.BaseScreenView;

import java.math.BigDecimal;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;

class TideDetailsView extends BaseScreenView<TideDetailsScreen> {

  @BindView(R.id.loading) ProgressBar loading;
  @BindView(R.id.tideDetailsContent) View content;
  @BindView(R.id.currentWaterLevel) TextView currentWaterLevel;

  TideDetailsView(Context context) {
    super(context);
    inflate(context, R.layout.tide_detail, this);
    bind(this);
  }

  public void setLatestMeasuredTideHeight(BigDecimal latestMeasuredTideHeight) {
    content.setAlpha(0f);
    content.setVisibility(VISIBLE);
    loading.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        content.animate().alpha(1f).start();
        loading.setVisibility(GONE);
      }
    }).start();
    currentWaterLevel.setText(String.format("%f ft", latestMeasuredTideHeight));
  }

}
