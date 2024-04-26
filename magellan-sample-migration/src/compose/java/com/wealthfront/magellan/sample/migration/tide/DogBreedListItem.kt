package com.wealthfront.magellan.sample.migration.tide

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DogBreedListItem(breedName: String, goToDogDetails: (name: String) -> Unit) {
  Text(
    text = breedName,
    textAlign = TextAlign.Center,
    style = Typography().bodyMedium.copy(
      fontWeight = FontWeight.Bold,
      fontSize = 20.sp,
      color = MaterialTheme.colorScheme.primary
    ),
    modifier = Modifier
      .clickable {
        goToDogDetails(breedName)
      }
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface)
      .padding(
        horizontal = 16.dp,
        vertical = 16.dp
      )
  )
}
