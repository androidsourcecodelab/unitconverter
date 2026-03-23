package com.androidsourcecodelab.unitconverter.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androidsourcecodelab.unitconverter.data.UnitRepository
import com.androidsourcecodelab.unitconverter.engine.ConversionStrategyFactory
import com.androidsourcecodelab.unitconverter.engine.format.FormatStrategyFactory
import com.androidsourcecodelab.unitconverter.viewmodel.ConverterViewModel
import com.yourpackage.unitconverter.ui.UnitDropdown


@Composable
fun ConverterScreen(viewModel: ConverterViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text(
            text = "Smart Unit Converter",
            style = MaterialTheme.typography.headlineMedium
        )

        CategoryGrid(
            categories = viewModel.state.categories,   // 🔥 not from VM
            selectedCategory = viewModel.state.category,
            onCategorySelected = { viewModel.setCategory(it) }
        )



        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // 🔹 Your existing Row (unchanged)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = viewModel.state.rawInputText,
                    onValueChange = { viewModel.onInputChanged(it) },
                    label = {
                        Text(
                            text = "Enter value or command",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Ascii
                    ),
                    trailingIcon = {
                        if (viewModel.state.rawInputText.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearInput() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear input"
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { viewModel.onConvert() },
                    enabled = viewModel.state.rawInputText.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Convert"
                    )
                }
            }

            // 🔥 ERROR MESSAGE (AFTER ROW)
            if (!viewModel.state.errorMessage.isNullOrEmpty()) {

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = viewModel.state.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
        }

        // 🔥 ADD THIS BELOW ROW (not inside it)
        if (viewModel.state.suggestions.isNotEmpty()) {

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                items(viewModel.state.suggestions) { unit ->

                    SuggestionChip(
                        onClick = { viewModel.applySuggestion(unit) },
                        label = { Text("to ${unit.symbol}") },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }



        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val stateunit = viewModel.state

            UnitDropdown(
                units = stateunit.category?.units ?: emptyList(),
                selected = stateunit.fromUnit,
                onSelected = { selectedUnit ->
                    viewModel.onFromUnitChanged(selectedUnit)
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

            val state = viewModel.state

            UnitDropdown(
                units = state.category?.units ?: emptyList(),
                selected = state.toUnit,
                onSelected = { selectedUnit ->
                    viewModel.onToUnitChanged(selectedUnit)
                }
            )
        }

        val state = viewModel.state

        ConversionResultScreen(
            state = state,
            onCopy = { /* optional analytics */ },
            favorites = viewModel.favorites,
            onFavoriteClick = { viewModel.applyFavorite(it) },
            onRemoveFavorite = { viewModel.removeFavorite(it) }
        )


        val category = state.category
        val from = state.fromUnit
        val to = state.toUnit

        if (category?.supportsNearby == true && from != null && to != null) {

            val baseValue = state.parsedValue.toDoubleOrNull()

            if (baseValue != null) {

                val nearbyValues = viewModel.generateNearbyValues(baseValue)

                val formatter = FormatStrategyFactory.get(category)
                val strategy = ConversionStrategyFactory.getStrategy(category)

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Text(
                        text = "Quick conversions",
                        style = MaterialTheme.typography.titleMedium
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        items(nearbyValues) { value ->

                            val converted = strategy.convert(
                                value.toString(),
                                from,
                                to
                            )

                            val formattedValue = formatter.format(value)
                            val formattedConverted = formatter.format(converted)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {

                                        val newInput = formatter.format(value)

                                        // ✅ update input ONLY
                                        viewModel.onInputChanged(newInput)

                                        // 🔥 optional but recommended
                                        // clears stale result + error
                                        viewModel.clearResultOnly()

                                        // ❌ DO NOT call onConvert()
                                    }
                                    .padding(8.dp)
                            ) {

                                Text(
                                    text = "$formattedValue ${from.symbol} = $formattedConverted ${to.symbol}"
                                )
                            }
                        }
                    }
                }
            }
        }// end of if

    }
}
