package com.androidsourcecodelab.unitconverter.ui
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.items


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.androidsourcecodelab.unitconverter.viewmodel.ConverterViewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import com.androidsourcecodelab.unitconverter.data.UnitRepository
import kotlin.collections.find

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultCard(viewModel: ConverterViewModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        if (viewModel.input.isNotEmpty()) {
            val cleanInput =
                viewModel.input.toDoubleOrNull()?.let {
                    viewModel.formatNumber(it)
                } ?: viewModel.input

            val resultText = buildAnnotatedString {

                append("$cleanInput ${viewModel.fromUnit.symbol} = ")

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(viewModel.result)
                }

                append(" ${viewModel.toUnit.symbol}")
            }

            Text(
                text = resultText,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    if (viewModel.favorites.isNotEmpty()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Favorite conversions",
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {

                items(
                    items = viewModel.favorites,
                    key = { "${it.from}-${it.to}" }
                ) { fav ->

                    AssistChip(
                        modifier = Modifier.padding(vertical = 2.dp),

                        onClick = {
                            viewModel.applyFavorite(fav)
                        },

                        label = {
                            Text(
                                text = "${fav.from} → ${fav.to}",
                                color = Color.Black
                            )
                        },

                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    viewModel.removeFavorite(fav)
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Remove favorite",
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                        },

                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = Color.Black,
                            containerColor = Color.White
                        )

                    )
                }
            }// lazy row
        }


    }



}