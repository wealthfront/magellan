package com.wealthfront.magellan.transitions

import android.view.View
import com.wealthfront.magellan.Direction

/**
 * Define a transition (animation) between two screens. By default, transitions are implemented by
 * [DefaultTransition]. You can either set a different default one when building your Navigator, using
 * [Navigator.Builder.transition],
 * or override the next transition by using
 * [Navigator.overrideTransition].
 *
 *
 * You can find more Transitions implemented in [com.wealthfront.magellan.transitions].
 */
interface MagellanTransition {

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
  fun animate(
    from: View?,
    to: View,
    direction: Direction,
    callback: Callback
  )

  interface Callback {
    fun onAnimationEnd()
  }
}
