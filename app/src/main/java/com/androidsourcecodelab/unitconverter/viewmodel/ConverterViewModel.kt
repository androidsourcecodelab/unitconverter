package com.androidsourcecodelab.unitconverter.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import java.text.DecimalFormat
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidsourcecodelab.unitconverter.FavoriteConversion
import com.androidsourcecodelab.unitconverter.data.FavoritesRepository
import com.androidsourcecodelab.unitconverter.data.UnitRepository
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class ConverterViewModel(application: Application) : ViewModel() {
    private val favoritesRepository =
        FavoritesRepository(application)
    var favorites by mutableStateOf(listOf<FavoriteConversion>())
    public val formatter = DecimalFormat("#,###.####")

    var input by mutableStateOf("")
    var result by mutableStateOf("")

    var selectedCategory by mutableStateOf(UnitRepository.categories.first())

    var fromUnit by mutableStateOf(selectedCategory.units.first())
    var toUnit by mutableStateOf(selectedCategory.units[1])

    init {

        viewModelScope.launch {

            favorites =
                favoritesRepository.loadFavorites()

        }
    }


    fun convert() {

        val value = input.toDoubleOrNull() ?: run {
            result = ""
            return
        }

        val base = value * fromUnit.factor
        val output = base / toUnit.factor

        result = formatter.format(output)
    }

    fun changeCategory(category: UnitCategory) {

        selectedCategory = category

        fromUnit = category.units.first()
        toUnit = category.units[1]

        convert()
    }

    fun swapUnits() {

        val temp = fromUnit
        fromUnit = toUnit
        toUnit = temp

        convert()
    }

    fun generateNearbyValues(value: Double): List<Double> {

        if (value == 0.0) return emptyList()

        val magnitude = 10.0.pow(floor(log10(value)))
        val step = magnitude / 10

        return listOf(
            value - 2 * step,
            value - step,
            value,
            value + step,
            value + 2 * step
        ).filter { it >= 0 }
    }

    fun addFavorite() {

        val favorite = FavoriteConversion(
            fromUnit.symbol,
            toUnit.symbol
        )

        if (!favorites.contains(favorite)) {

            favorites = (favorites + favorite).takeLast(6)

            viewModelScope.launch {
                favoritesRepository.saveFavorites(favorites)
            }
        }
    }

    fun removeFavorite(favorite: FavoriteConversion) {

        favorites = favorites - favorite

        viewModelScope.launch {
            favoritesRepository.saveFavorites(favorites)
        }
    }
}