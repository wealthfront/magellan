package com.wealthfront.magellan.transitions;

import android.view.View;

import com.wealthfront.magellan.Direction;
import com.wealthfront.magellan.NavigationType;
import com.wealthfront.magellan.Navigator;

/**
 * Define a transition (animation) between two screens. By default, transitions are implemented by
 * {@link DefaultTransition}. You can either set a different default one when building your Navigator, using
 * {@link Navigator.Builder#transition(Transition)},
 * or override the next transition by using
 * {@link Navigator#overrideTransition(Transition)}.
 * <p>
 * You can find more Transitions implemented in {@link com.wealthfront.magellan.transitions}.
 */
public interface Transition {

  /**
   * Animate between 2 views (associated to the screens).
   *
   * @param from the view of the screen we are coming from
   * @param to the view of the screen we are going to
   * @param navType the type of navigation that is happening, see {@link NavigationType}
   * @param direction the direction of the navigation, see {@link Direction}
   * @param callback the callback to call when the animation is done. You <b>must</b> call
   * {@link Callback#onAnimationEnd()} at the end of your animation.
   */
  void animate(View from, View to, NavigationType navType, Direction direction, Callback callback);

  interface Callback {
    void onAnimationEnd();
  }

}
