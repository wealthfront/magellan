# Changelog

## 2.0-alpha
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
