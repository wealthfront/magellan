# Changelog

## 2.1.1
  - Minor fix: Update lifecycle logic when attaching to an Activity

## 2.1.0
  - Release candidate for pushing to production
  - Minor bug fixes
    - Fix dialogs to show onResume
    - Fix memory leaks
  - Add shown coroutine scope to Step and screen

## 2.0.2-beta
  - Improved debugging
    - Add state printer to get the lifecycle snapshot
    - Improve the visualization of the backstack
  - Added lifecycle limiter to better control over the lifecycle of the lifecycle objects
  - Fix issues with the navigation delegate

## 2.0.1-beta
  - Improve testability
    - Open classes for mockable
    - Support for testing overloaded methods in the Navigator 

## 2.0.0-beta
  - Added backwards compatibility for
    - All the navigation methods
    - ScreenLifecycleListener
    - History re-writer
    - NavigationType
  - Added ability to turn off animations and logging
  - Added ability to provide default transitions
  - Breaking changes:
    - Removed action bar related code
    - Removed activity reference from screens

## 2.0-1-alpha
  - Introduced journeys/steps which help with implementation the concept of flows
  - Add support for lifecycle outside of screens/navigables
  - Breaking changes:
    - Added nullability annotation
    - Replaced NavigationType with MagellanTransition
    - API change for History rewriting
  - And a lot more...

## 1.1.0
  - Add support for RxJava 2 in `magellan-rx2` (thanks [@FabianTerhorst](https://github.com/FabianTerhorst))
  - Add `whenTransitionFinished(TransitionFinishedListener)` method to delay the execution of a code block until after
  the transition into the current screen is finished. This allows one to, for example, delay view updates until the
  transition has finished to avoid dropping frames.
  - Add `Navigator.goBackTo()` with navigation type
  - Add `Screen.setTitle(CharSequence)` instead of requiring a string resource
  - Add `Screen.inflate()` without context argument (thanks [@theyann](https://github.com/theyann))
