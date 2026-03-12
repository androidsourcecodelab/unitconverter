package com.androidsourcecodelab.unitconverter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.androidsourcecodelab.unitconverter.data.UnitRepository
import com.androidsourcecodelab.unitconverter.model.ConverterType
import com.androidsourcecodelab.unitconverter.util.UnitAliasResolver
import com.androidsourcecodelab.unitconverter.viewmodel.ConverterViewModel
import com.yourpackage.unitconverter.ui.UnitDropdown

class ConverterScreen {

}
@Composable
fun ConverterScreen(viewModel: ConverterViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = "Unit Converter",
            style = MaterialTheme.typography.headlineMedium
        )

        CategoryDropdown(viewModel)

        OutlinedTextField(
            value = viewModel.input,
            onValueChange = { newValue ->

                val (numberPart, detectedUnit) =
                    UnitAliasResolver.parseInput(newValue)

                // Guard: ignore input with no numeric part
                //if (numberPart.isEmpty()) {


                if (numberPart.isNotEmpty()) {
                    // validate numeric part
                    if (numberPart.matches(Regex("^\\d*\\.?\\d*$"))) {

                        viewModel.input = numberPart

                        if (detectedUnit != null) {

                            val result = UnitRepository.findUnitAcrossCategories(detectedUnit)

                            if (result != null) {

                                val (category, unit) = result

                                viewModel.selectedCategory = category
                                viewModel.fromUnit = unit
                                viewModel.toUnit = category.units.first { it.symbol != unit.symbol }

                            }
                        }

                        viewModel.convert()
                    }
                }
            },
            label = { Text("Enter value") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            trailingIcon = {

                if (viewModel.input.isNotEmpty()) {

                    IconButton(
                        onClick = {
                            viewModel.input = ""
                            viewModel.result = ""
                        }
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Clear input"
                        )
                    }

                }
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            UnitDropdown(
                units = viewModel.selectedCategory.units,
                selected = viewModel.fromUnit,
                onSelected = {

                    if (it == viewModel.toUnit) {
                        viewModel.swapUnits()
                    } else {
                        viewModel.fromUnit = it
                        viewModel.convert()
                    }

                }
            )

            IconButton(onClick = { viewModel.swapUnits() }) {
                Icon(Icons.Filled.SwapHoriz, contentDescription = "Swap")
            }

            IconButton(onClick = { viewModel.addFavorite() }) {

                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Add favorite"
                )

            }

            UnitDropdown(
                units = viewModel.selectedCategory.units,
                selected = viewModel.toUnit,
                onSelected = {

                    if (it == viewModel.fromUnit) {
                        viewModel.swapUnits()
                    } else {
                        viewModel.toUnit = it
                        viewModel.convert()
                    }

                }
            )
        }

        ResultCard(viewModel)

        if (viewModel.input.isNotEmpty()) {

            val inputValue = viewModel.input.toDoubleOrNull()
            if (viewModel.selectedCategory.type== ConverterType.LINEAR && viewModel.selectedCategory.name!=("Data Size")) {

                if (inputValue != null) {

                    val nearbyValues = viewModel.generateNearbyValues(inputValue)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Nearby conversions",
                            style = MaterialTheme.typography.titleMedium
                        )

                        nearbyValues.forEach { value ->

                            val base = value * viewModel.fromUnit.factor
                            val converted = base / viewModel.toUnit.factor

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {

                                        viewModel.input = viewModel.formatter.format(value)
                                        viewModel.convert()

                                    }
                                    .padding(vertical = 6.dp)
                            ) {

                                Text(
                                    text = "${viewModel.formatNumber(value)} ${viewModel.fromUnit.symbol} = " +
                                            "${viewModel.formatNumber(converted)} ${viewModel.toUnit.symbol}",
                                    modifier = Modifier.clickable {

                                        viewModel.input = viewModel.formatter.format(value)
                                        viewModel.convert()

                                    }

                                )
                            }// end of text
                        }
                    }
                } // end of if
            }
        }

    }
}
