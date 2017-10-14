package com.wealthfront.magellan.kotlin

/**
 * The interface to be implemented by a View representing a Screen.
 * @see BaseScreenView
 */
interface ScreenView<S> where S : Screen<*> {

  fun setScreen(screen: S)

  fun getScreen(): S

}