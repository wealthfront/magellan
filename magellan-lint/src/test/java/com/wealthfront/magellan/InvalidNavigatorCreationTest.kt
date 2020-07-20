package com.wealthfront.magellan

import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class InvalidNavigatorCreationTest {

  private val NAVIGATOR = kt(
    """
    package com.wealthfront.magellan.navigation

    interface Navigator {
    
      val backStack: List<NavigationEvent>
    }
  """).indented()

  private val LINEAR_NAVIGATOR = kt(
    """
    package com.wealthfront.magellan.navigation

    class LinearNavigator: Navigator {
    
      val backStack: List<NavigationEvent>
    }
  """).indented()

  private val JOURNEY = kt(
    """
    package com.wealthfront.magellan.core

    import com.wealthfront.magellan.navigation.LinearNavigator
    
    abstract class Journey<V>(createBinding: () -> V) {
    
      protected var navigator by lifecycle(LinearNavigator { viewBinding!!.container() })
    }

  """).indented()

  @Test
  fun testNavigatorCreationInJourney() {
    lint()
      .files(NAVIGATOR, LINEAR_NAVIGATOR, JOURNEY, kt("""
          package com.wealthfront.magellan.app

          import com.wealthfront.magellan.core.Journey
          import com.wealthfront.magellan.navigation.LinearNavigator
          
          class NewJourney: Journey<Unit> {
          
            val navigator: LinearNavigator? = null
          }
        """).indented())
      .issues(INVALID_NAVIGATOR_CREATION)
      .run()
      .expect("src/com/wealthfront/magellan/app/NewJourney.kt:8: Warning: Use the navigator provided by the Journey instead of creating your own instance. [InvalidNavigatorCreation]\n" +
        "  val navigator: LinearNavigator? = null\n" +
        "  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "0 errors, 1 warnings")
      .expectFixDiffs("Fix for src/com/wealthfront/magellan/app/NewJourney.kt line 8: Remove navigator instance:\n" +
        "@@ -8 +8\n" +
        "-   val navigator: LinearNavigator? = null")
  }

  @Test
  fun testNavigatorCreationWithinJourneyWithDelegates() {
    lint()
      .files(NAVIGATOR, LINEAR_NAVIGATOR, JOURNEY, kt(
        """
          package com.wealthfront.magellan.app

          import com.wealthfront.magellan.core.Journey
          import com.wealthfront.magellan.navigation.LinearNavigator
          
          class NewJourney: Journey<Unit> {
          
            val navigator by lazy { LinearNavigator() }
          }
        """).indented())
      .issues(INVALID_NAVIGATOR_CREATION)
      .run()
      .expect("src/com/wealthfront/magellan/app/NewJourney.kt:8: Warning: Use the navigator provided by the Journey instead of creating your own instance. [InvalidNavigatorCreation]\n" +
        "  val navigator by lazy { LinearNavigator() }\n" +
        "  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "0 errors, 1 warnings")
      .expectFixDiffs("Fix for src/com/wealthfront/magellan/app/NewJourney.kt line 8: Remove navigator instance:\n" +
        "@@ -8 +8\n" +
        "-   val navigator by lazy { LinearNavigator() }")
  }

  @Test
  fun testNavigatorCreationOutsideJourney() {
    lint()
      .files(NAVIGATOR, LINEAR_NAVIGATOR, kt("""
          package com.wealthfront.magellan.app

          import com.wealthfront.magellan.navigation.LinearNavigator
          
          class NewJourney {
          
            val navigator: LinearNavigator? = null
          }
        """).indented())
      .issues(INVALID_NAVIGATOR_CREATION)
      .run()
      .expectClean()
  }
}
