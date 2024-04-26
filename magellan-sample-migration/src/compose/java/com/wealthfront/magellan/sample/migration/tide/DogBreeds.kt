package com.wealthfront.magellan.sample.migration.tide

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun DogBreeds(dogBreeds: List<String>, onBreedClick: (name: String) -> Unit) {
  LazyColumn(modifier = Modifier.testTag("DogBreeds")) {
    itemsIndexed(dogBreeds) { index, item ->
      DogBreedListItem(item, onBreedClick)
      if (index != (dogBreeds.size - 1)) {
        HorizontalDivider()
      }
    }
  }
}
