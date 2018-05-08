package com.wealthfront.magellan.transitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.wealthfront.magellan.Direction;
import com.wealthfront.magellan.NavigationType;

import static com.wealthfront.magellan.Direction.BACKWARD;
import static com.wealthfront.magellan.Direction.FORWARD;

public class DefaultTransition implements Transition {

  private TimeInterpolator customGoInterpolator = new AccelerateDecelerateInterpolator();
  private TimeInterpolator customShowInterpolator = new AccelerateDecelerateInterpolator();
  private TimeInterpolator customHideInterpolator = new AccelerateDecelerateInterpolator();

  public DefaultTransition() { }

  public DefaultTransition(TimeInterpolator customInterpolator) {
    this.customGoInterpolator = customInterpolator;
  }

  public DefaultTransition(TimeInterpolator customGoInterpolator, TimeInterpolator customShowInterpolator,
                           TimeInterpolator customHideInterpolator) {
    this.customGoInterpolator = customGoInterpolator;
    this.customShowInterpolator = customShowInterpolator;
    this.customHideInterpolator = customHideInterpolator;
  }

  @Override
  public final void animate(View from, View to, NavigationType navType, Direction direction, final Callback callback) {
    AnimatorSet animator = createAnimator(from, to, navType, direction);
    animator.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        callback.onAnimationEnd();
      }
    });
    animator.start();
  }

  private AnimatorSet createAnimator(View from, View to, NavigationType navType, Direction direction) {
    Property<View, Float> axis;
    int fromTranslation;
    int toTranslation;
    int sign = direction.sign();

    switch (navType) {
      case GO:
        axis = View.TRANSLATION_X;
        fromTranslation = sign * -from.getWidth();
        toTranslation = sign * to.getWidth();
        break;
      case SHOW:
        axis = View.TRANSLATION_Y;
        fromTranslation = direction == FORWARD ? 0 : from.getHeight();
        toTranslation = direction == BACKWARD ? 0 : to.getHeight();
        break;
      default:
        axis = View.TRANSLATION_X;
        fromTranslation = 0;
        toTranslation = 0;
        break;
    }
    AnimatorSet set = new AnimatorSet();
    ObjectAnimator animator;
    if (from != null) {
      animator = ObjectAnimator.ofFloat(from, axis, 0, fromTranslation);
      animator.setInterpolator(customGoInterpolator);
      set.play(animator);
    }
    animator = ObjectAnimator.ofFloat(to, axis, toTranslation, 0);
    animator.setInterpolator(customGoInterpolator);
    set.play(animator);
    return set;
  }
}
