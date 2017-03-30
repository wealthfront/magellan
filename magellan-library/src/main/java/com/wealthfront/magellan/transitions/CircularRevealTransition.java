package com.wealthfront.magellan.transitions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.wealthfront.magellan.Direction;
import com.wealthfront.magellan.NavigationType;

public class CircularRevealTransition implements Transition {

  private final View clickedView;

  public CircularRevealTransition(View clickedView) {
    this.clickedView = clickedView;
  }

  @Override
  public void animate(
      View from, View to, NavigationType navType, Direction direction, final Callback callback) {
    int[] clickedViewCenter = getCenterClickedView((ViewGroup) from);
    int circularRevealCenterX = clickedViewCenter[0];
    int circularRevealCenterY = clickedViewCenter[1];
    float finalRadius = (float) Math.hypot(to.getWidth(), to.getHeight());

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      Animator anim =
          ViewAnimationUtils.createCircularReveal(to, circularRevealCenterX,
              circularRevealCenterY, 0, finalRadius);
      anim.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          callback.onAnimationEnd();
        }
      });
      anim.start();
    } else {
      callback.onAnimationEnd();
    }
  }

  private int[] getCenterClickedView(ViewGroup from) {
    Rect clickedViewRect = new Rect();
    clickedView.getDrawingRect(clickedViewRect);
    from.offsetDescendantRectToMyCoords(clickedView, clickedViewRect);
    return new int[] {(int) clickedViewRect.exactCenterX(), (int) clickedViewRect.exactCenterY()};
  }

}
