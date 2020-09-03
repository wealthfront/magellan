package com.wealthfront.magellan

import com.android.tools.lint.checks.infrastructure.TestFiles.kt
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class AvoidUsingActivityTest {

  private val NAVIGABLE_COMPAT = kt(
    """
    package com.wealthfront.magellan.navigation

    import android.app.Activity

    interface NavigableCompat {
    
      val activity: Activity? = null
      
    }


  """).indented()

  private val ACTIVITY = kt(
    """
    package android.app
    
    class Activity {
    
      fun setTitle() {}
    }
  """).indented()

  private val ACTIVITY_FILES = arrayOf(NAVIGABLE_COMPAT, ACTIVITY)

  @Test
  fun testThatIssueIsDetected() {
    lint()
      .files(*ACTIVITY_FILES, kt(
        """
          package com.wealthfront.magellan.app
          
          import com.wealthfront.magellan.navigation.NavigableCompat
          
          class SomeClass : NavigableCompat {
          
            fun someFunc() {
              activity.setTitle()
            }
          }
        """).indented())
      .issues(AVOID_USING_ACTIVITY)
      .run()
      .expect("src/com/wealthfront/magellan/app/SomeClass.kt:8: Warning: Avoid using the activity instance present in the superclass. Instead use the context provided in the lifecycle methods. [AvoidUsingActivity]\n" +
        "    activity.setTitle()\n" +
        "    ~~~~~~~~~~~~~~~~~~~\n" +
        "0 errors, 1 warnings")
  }

  @Test
  fun testThatIssueIsNotDetected() {
    lint()
      .files(*ACTIVITY_FILES, kt("""
          package com.wealthfront.magellan.app

          import com.wealthfront.magellan.lifecycle.LifecycleAware
                    import com.wealthfront.magellan.navigation.LinearNavigator

          class SomeClass : NavigableCompat {
                    
            fun someFunc() {
            }
          }
        """).indented())
      .issues(AVOID_USING_ACTIVITY)
      .run()
      .expectClean()
  }
}
