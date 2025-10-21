# Changelog

## 2.2.9-beta
- Support transition interruption in `NavigationDelegate`

## 2.2.8-beta
- Support removal from `LazySetNavigator`

## 2.2.7-beta
- Fixes crash during synchronous navigation in `LazySetNavigator`
- Support backstack read operations from a background thread

## 2.2.6-beta
- Fixes `CircularRevealTransition` attempting to target non-existent views

## 2.2.5-beta
- Fixes `CircularRevealTransition` leaking memory and crashing on back press.

## 2.2.4-beta
- Fixes `DialogComponent` not displaying dialog when `showDialog` called before `resume`

## 2.2.3-beta
- Fixes `onBackPressed` called twice for navigables at the top of a navigator's backstack
- Support non-Activity contexts (ie. ContextWrapper)

## 2.2.2-beta
- Adds extension functions to `LinearNavigator` for common navigation patterns.

## 2.2.1-beta
- Support interrupting transitions to prevent overlapping animations in `LazySetNavigator`

## 2.2.0-beta
- Make `currentNavigable` a recursive function of the current backstack
- Move `afterNavigation` trigger to post-transition animation
- Support custom navigable behavior in `NavigationTraverser`

## 2.1.10-beta
- Introduce `LazySetNavigator` for driving the lifecycle of previously `NO_LIMIT`-ed navigables with a floor of `SHOWN`, rather than `CREATED` 

## 2.1.9-beta
- Add `whenTransitionFinished(TransitionFinishedListener)` method to `Step`, supporting an API available for `Screen`
- Introduce `LegacyJourney` for easier migration from 1.x. Similar to `LegacyExpedition`, this construct allows nesting
  `Screen` inside of a `Journey`

## 2.1.8-beta
- Support non-idempotent navigation operations for `NavigationRequestHandler`. Operations will now be run once at most.

## 2.1.7-beta
- Add `ViewTemplateApplier` to manipulate Navigable views at Navigator level

## 2.1.6-beta
- Don't expose dialog state publicly
- Add `NavigationRequestHandler` to override navigation intents

## 2.1.5-beta
  - Add `LegacyStep` for easier migration from 1.x
    - Includes most things that a `Screen` had, including a similar `Screen`/`View` class split
  - Open up navigator to allow for easier extension

## 2.1.4-beta
  - Further improve testing support
    - Make `LinearNavigator` an interface, and make `DefaultLinearNavigator`'s constructor public
    - Create a `FakeLinearNavigator` as a test double, in the `magellan-test` module
  - Create a `SimpleJourney` for `Journey`s that don't need custom display logic
  - Misc
    - Fix a crash when navigating while the app is in the background
    - Fix exception message for duplicate `Navigable`s in a `DefaultLinearNavigator` backstack
    - Update built-in transitions to use `FastInSlowOutInterpolator`

## 2.1.3-beta
  - Improve testing support
    - Make `Journey.navigator` `@VisibleForTesting` so we can access it in tests for verification
    - Make `transition` methods public, move from `LifecycleStateMachine` to extension functions on `LifecycleAware`
    - Detach `LifecycleAware` objects attached using `by attachFieldToLifecycle` when field is set (usually in tests)
    - [Full PR here](https://github.com/wealthfront/magellan/pull/204)
  - Rename lifecycle-attaching delegates
    - `by lifecycle(...)` -> `by attachFieldToLifecycle(...)`
    - `by lateinitLifecycle(..)` -> `by attachLateinitFieldToLifecycle(...)`
    - `by lifecycleWithContext(...)` -> `by createAndAttachFieldToLifecycleWhenShown(...)`
  - Rename some constructor parameters
    - `Step(createBinding: (LayoutInflater) -> V)` -> `Step(inflateBinding: (LayoutInflater) -> V)`
    - `Journey(createBinding: (LayoutInflater) -> V, container: (V) -> ScreenContainer)` -> `Journey(inflateBinding: (LayoutInflater) -> V, getContainer: (V) -> ScreenContainer)`

## 2.1.2 (beta)
  - Minor fix: Fix overlapping activities bug

## 2.1.1 (beta)
  - Minor fix: Update lifecycle logic when attaching to an Activity

## 2.1.0 (beta)
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
