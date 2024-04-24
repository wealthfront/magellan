package com.wealthfront.magellan.sample.migration.tide

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable

@Composable
fun DogBreeds(dogBreeds: List<String>, onBreedClick: (name: String) -> Unit) {
  LazyColumn {
    itemsIndexed(dogBreeds) { index, item ->
      DogBreedListItem(item, onBreedClick)
      if (index != (dogBreeds.size - 1)) {
        HorizontalDivider()
      }
    }
  }
}
