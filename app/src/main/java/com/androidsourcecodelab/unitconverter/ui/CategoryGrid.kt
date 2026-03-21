package com.androidsourcecodelab.unitconverter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.androidsourcecodelab.unitconverter.model.UnitCategory

@Composable
fun CategoryGrid(
    categories: List<UnitCategory>,
    selectedCategory: UnitCategory?,
    onCategorySelected: (UnitCategory) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

        items(categories) { category ->

            CategoryTile(
                name = category.name,
                label = category.iconLabel,
                selected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryTile(
    name: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = iconForCategory(name),
                contentDescription = name,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun iconForCategory(name: String): ImageVector {
    return when (name) {
        "Length" -> Icons.Default.Straighten
        "Weight" -> Icons.Default.Scale
        "Speed" -> Icons.Default.Speed
        "Area" -> Icons.Default.CropSquare
        "Volume" -> Icons.Default.LocalDrink
        "Time" -> Icons.Default.Schedule
        "Pressure" -> Icons.Default.Compress
        "Energy" -> Icons.Default.Bolt
        "Temperature" -> Icons.Default.Thermostat
        "Astronomy" -> Icons.Default.Public
        else -> Icons.Default.Category
    }
}
