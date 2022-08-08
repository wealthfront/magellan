package com.wealthfront.magellan

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class InvalidChildInScreenContainerTest {

  @Test
  fun testScreenContainerHavingChildren() {
    lint()
      .files(
        xml(
          "res/layout/main.xml",
          """
            <com.wealthfront.magellan.ScreenContainer
                xmlns:android="http://schemas.android.com/apk/res/android"
                android:id="@+id/container"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                >
                
                <android.widget.TextView
                   android:id="@+id/text"
                   android:layout_width="wrap_content"
                   android:layout_height="wrap_content" 
                   />
             
             </com.wealthfront.magellan.ScreenContainer>
          """
        ).indented()
      )
      .issues(INVALID_CHILD_IN_SCREEN_CONTAINER)
      .run()
      .expect(
        """
            res/layout/main.xml:1: Error: Remove child views inside the ScreenContainer. [InvalidChildInScreenContainer]
            <com.wealthfront.magellan.ScreenContainer
             ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            1 errors, 0 warnings
        """.trimIndent()
      )
  }

  @Test
  fun testScreenContainerWithoutChildren() {
    lint()
      .files(
        xml(
          "res/layout/main.xml",
          """
            <com.wealthfront.magellan.ScreenContainer
                xmlns:android="http://schemas.android.com/apk/res/android"
                android:id="@+id/container"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                />
          """
        ).indented()
      )
      .issues(INVALID_CHILD_IN_SCREEN_CONTAINER)
      .run()
      .expectClean()
  }
}
