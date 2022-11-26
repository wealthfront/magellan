package com.ryanmoelter.magellanx

import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import org.junit.Test

class EnforceLifecycleAwareAttachmentTest {

  private val LIFECYCLE_AWARE = kt(
    """
    package com.ryanmoelter.magellanx.lifecycle

    interface LifecycleAware {

      fun create() {}
    }

  """
  ).indented()

  private val LINEAR_NAVIGATOR = kt(
    """
    package com.ryanmoelter.magellanx.navigation

    import com.ryanmoelter.magellanx.lifecycle.LifecycleAware

    class LinearNavigator: LifecycleAware {

      val backStack: List<NavigationEvent> = listOf()
    }
  """
  ).indented()

  private val LIFECYCLE_OWNER = kt(
    """
      package com.ryanmoelter.magellanx.lifecycle

      interface LifecycleOwner {

        fun attachToLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState = LifecycleState.Destroyed)

        fun removeFromLifecycle(lifecycleAware: LifecycleAware, detachedState: LifecycleState = LifecycleState.Destroyed)
      }

  """
  ).indented()

  private val LIFECYCLE_FILES = arrayOf(LIFECYCLE_AWARE, LIFECYCLE_OWNER, LINEAR_NAVIGATOR)

  @Test
  fun testThatInstanceCreationIsDetected() {
    lint()
      .testModes(TestMode.DEFAULT)
      .allowMissingSdk()
      .files(
        *LIFECYCLE_FILES,
        kt(
          """
          package com.ryanmoelter.magellanx.app

          import com.ryanmoelter.magellanx.lifecycle.LifecycleOwner
          import com.ryanmoelter.magellanx.navigation.LinearNavigator

          class SomeClass : LifecycleOwner {

            val navigator = LinearNavigator()
          }
        """
        ).indented()
      )
      .issues(ENFORCE_LIFECYCLE_AWARE_ATTACHMENT)
      .run()
      .expect(
        "src/com/ryanmoelter/magellanx/app/SomeClass.kt:8: " +
          "Error: In order to make this lifecycle aware work as expected, please attach it to the lifecycle owner with a lifecycle delegate. " +
          "Eg. val someObject by lifecycle(SomeObject()) or lateinit var someObject: SomeObject by lateinitLifecycle() [EnforceLifecycleAwareAttachment]\n" +
          "  val navigator = LinearNavigator()\n" +
          "  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
          "1 errors, 0 warnings"
      )
      .expectFixDiffs("")
  }

  @Test
  fun testThatProperInstanceCreationIsNotDetected() {
    lint()
      .allowMissingSdk()
      .files(
        *LIFECYCLE_FILES,
        kt(
          """
          package com.ryanmoelter.magellanx.app

          import com.ryanmoelter.magellanx.lifecycle.LifecycleAware
                    import com.ryanmoelter.magellanx.navigation.LinearNavigator

          class SomeClass : LifecycleAware {

            val navigator by lifecycle(LinearNavigator())
          }
        """
        ).indented()
      )
      .issues(ENFORCE_LIFECYCLE_AWARE_ATTACHMENT)
      .run()
      .expectClean()
  }
}
