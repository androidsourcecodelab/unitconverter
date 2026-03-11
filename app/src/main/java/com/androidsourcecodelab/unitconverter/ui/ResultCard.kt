package com.androidsourcecodelab.unitconverter.ui
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.items


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.androidsourcecodelab.unitconverter.viewmodel.ConverterViewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import kotlin.collections.find

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

            val resultText = buildAnnotatedString {

                append("${viewModel.input} ${viewModel.fromUnit.symbol} = ")

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

                items(viewModel.favorites) { fav ->

                    AssistChip(
                        onClick = {

                            val from = viewModel.selectedCategory.units
                                .find { it.symbol == fav.from }

                            val to = viewModel.selectedCategory.units
                                .find { it.symbol == fav.to }

                            if (from != null && to != null) {
                                viewModel.fromUnit = from
                                viewModel.toUnit = to
                                viewModel.convert()
                            }

                        },
                        label = { Text("${fav.from} → ${fav.to}") },
                        modifier = Modifier.pointerInput(Unit) {

                            detectTapGestures(
                                onLongPress = {
                                    viewModel.removeFavorite(fav)
                                }
                            )

                        }
                    )

                }
            }  // lazy row end
        }


    }



}