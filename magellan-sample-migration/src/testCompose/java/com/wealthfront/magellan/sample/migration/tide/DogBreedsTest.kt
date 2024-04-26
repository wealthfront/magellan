package com.wealthfront.magellan.sample.migration.tide

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DogBreedsTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun goesToSelectedDogBreed() {
    var clicked = false
    composeTestRule.setContent {
      DogBreeds(dogBreeds = listOf("akita"), onBreedClick = { clicked = true })
    }

    composeTestRule.onNodeWithTag("DogBreeds").onChildAt(0).performClick()

    assertThat(clicked).isTrue()
  }
}
