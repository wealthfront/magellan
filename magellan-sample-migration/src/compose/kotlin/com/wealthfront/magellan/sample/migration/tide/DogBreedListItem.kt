package com.wealthfront.magellan.sample.migration.tide

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DogBreedListItem(breedName: String) {
  Text(
    text = breedName,
    style = Typography().bodyMedium,
    modifier = Modifier
      .fillMaxWidth()
      .padding(
        horizontal = 16.dp,
        vertical = 8.dp
      )
  )
}
