package com.wealthfront.magellan.kotlin.transitions

import android.view.View
import com.wealthfront.magellan.kotlin.Direction
import com.wealthfront.magellan.kotlin.NavigationType

/**
 * Define a transition (animation) between two screens. By default, transitions are implemented by
 * [DefaultTransition]. You can either set a different default one when building your Navigator, using
 * [Navigator.Builder.transition],
 * or override the next transition by using
 * [Navigator.overrideTransition].
 *
 *
 * You can find more Transitions implemented in [com.wealthfront.magellan.kotlin.transitions].
 */
interface Transition {

  /**
   * Animate between 2 views (associated to the screens).
   *
   * @param from the view of the screen we are coming from
   * @param to the view of the screen we are going to
   * @param navType the type of navigation that is happening, see [NavigationType]
   * @param direction the direction of the navigation, see [Direction]
   * @param callback the callback to call when the animation is done. You **must** call
   * [Callback.onAnimationEnd] at the end of your animation.
   */
  fun animate(from: View, to: View, navType: NavigationType, direction: Direction, callback: Callback)

  interface Callback {
    fun onAnimationEnd()
  }

}