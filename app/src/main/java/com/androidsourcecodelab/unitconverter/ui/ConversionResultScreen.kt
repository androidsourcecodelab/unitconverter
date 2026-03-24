package com.androidsourcecodelab.unitconverter.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.androidsourcecodelab.unitconverter.FavoriteConversion
import com.androidsourcecodelab.unitconverter.engine.format.FormatStrategyFactory
import com.androidsourcecodelab.unitconverter.viewmodel.UiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversionResultScreen(
    state: UiState,
    onCopy: (String) -> Unit,
    favorites: List<FavoriteConversion>,
    onFavoriteClick: (FavoriteConversion) -> Unit,
    onRemoveFavorite: (FavoriteConversion) -> Unit
) {

    val from = state.fromUnit
    val to = state.toUnit
    val category = state.category

    if (state.result.isNotEmpty() && from != null && to != null && category != null) {

        val formatter = FormatStrategyFactory.get(category)

        val inputText = if (state.isComposite && state.compositeComponents != null) {
            formatComponents(state.compositeComponents)
        } else {
            "${state.parsedValue} ${from.symbol}"
        }

        val resultText = buildAnnotatedString {

            append("$inputText = ")

            withStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold)
            ) {
                append(formatter.format(state.result))
            }

            append(" ${to.symbol}")
        }

        val clipboardManager = LocalClipboardManager.current

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = resultText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        clipboardManager.setText(resultText)
                        onCopy(resultText.text)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy"
                    )
                }
            }
        }
    }

    // 🔥 Favorites (decoupled)
    if (favorites.isNotEmpty()) {

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
                    items = favorites,
                    key = { "${it.from}-${it.to}" }
                ) { fav ->

                    AssistChip(
                        modifier = Modifier.padding(vertical = 2.dp),

                        onClick = { onFavoriteClick(fav) },

                        label = {
                            Text("${fav.from} → ${fav.to}")
                        },

                        trailingIcon = {
                            IconButton(
                                onClick = { onRemoveFavorite(fav) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Remove favorite",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        },

                        colors = AssistChipDefaults.assistChipColors()
                    )
                }
            }
        }
    }
}

private fun formatComponents(
    components: List<Pair<Double, String>>
): String {
    return components.joinToString(" ") { (value, unit) ->

        val displayValue =
            if (value % 1.0 == 0.0) value.toInt().toString()
            else value.toString()

        "$displayValue $unit"
    }
}