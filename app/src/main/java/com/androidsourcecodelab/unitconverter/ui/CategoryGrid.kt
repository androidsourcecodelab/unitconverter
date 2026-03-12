package com.androidsourcecodelab.unitconverter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidsourcecodelab.unitconverter.viewmodel.ConverterViewModel

@Composable
fun CategoryGrid(viewModel: ConverterViewModel) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        items(viewModel.categories) { category ->

            AssistChip(
                onClick = {
                    viewModel.setCategory(category)
                },
                label = {
                    Text(
                        text = category.name,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                modifier = Modifier.padding(4.dp),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor =
                        if (category == viewModel.selectedCategory)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                )
            )

        }

    }

}