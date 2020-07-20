package com.wealthfront.magellan.transitions;

import android.view.View;

import com.wealthfront.magellan.Direction;
import com.wealthfront.magellan.NavigationType;

public class NoAnimationTransition implements Transition {

  @Override
  public void animate(
      View from, View to, NavigationType navType, Direction direction, Callback callback) {
    callback.onAnimationEnd();
  }

}
