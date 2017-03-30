package com.wealthfront.magellan.transitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

import com.wealthfront.magellan.Direction;
import com.wealthfront.magellan.NavigationType;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CrossfadeTransition implements Transition {

  @Override
  public void animate(
      final View from, final View to, NavigationType navType, Direction direction, final Callback callback) {
    to.setAlpha(0f);
    from.setAlpha(1f);
    to.setVisibility(VISIBLE);
    from.setVisibility(VISIBLE);

    from.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        from.setVisibility(GONE);
        to.animate().alpha(1f).setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            callback.onAnimationEnd();
          }
        }).start();
      }
    }).start();
  }

}
