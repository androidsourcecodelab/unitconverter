package com.androidsourcecodelab.unitconverter.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import java.text.DecimalFormat
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidsourcecodelab.unitconverter.FavoriteConversion
import com.androidsourcecodelab.unitconverter.data.BaseConverter
import com.androidsourcecodelab.unitconverter.data.FavoritesRepository
import com.androidsourcecodelab.unitconverter.data.UnitRepository
import com.androidsourcecodelab.unitconverter.model.ConverterType
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class ConverterViewModel(application: Application) : ViewModel() {
    val categories = UnitRepository.categories
    private val favoritesRepository =
        FavoritesRepository(application)
    var favorites by mutableStateOf(listOf<FavoriteConversion>())
    //var favorites = mutableStateListOf<Favorite>()
    public val formatter = DecimalFormat("#,###.####")
    private val scientificFormatter =
        DecimalFormat("0.###E0")

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
        if (selectedCategory.type == ConverterType.NUMBER_BASE) {
            if (selectedCategory.name == "Number Base") {

                result = BaseConverter.convert(
                    input,
                    fromUnit.symbol,
                    toUnit.symbol
                )

                return
            }
            return
        }

        val value = input.toDoubleOrNull() ?: run {
            result = ""
            return
        }

        val base = value * fromUnit.factor
        val output = base / toUnit.factor

        result = formatNumber(output)
    }

    fun formatNumber(value: Double): String {
        if (selectedCategory.name != "Number Base") {


            val abs = kotlin.math.abs(value)

            return if (abs >= 1_000_000 || abs <= 0.001 && abs != 0.0) {
                scientificFormatter.format(value)
            } else {
                formatter.format(value)
            }
        }
        else
            return value.toString()
    }

    fun convertNumberBase() {

        val value = input.toIntOrNull() ?: return

        result = when (toUnit.symbol) {

            "HEX" -> value.toString(16).uppercase()

            "BIN" -> value.toString(2)

            "OCT" -> value.toString(8)

            else -> value.toString()

        }
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
        Log.d("FavoritesRepo adding ",fromUnit.symbol+","+toUnit.symbol)
        if (favorites.size >= 5) return
        val favorite = FavoriteConversion(
            fromUnit.symbol,
            toUnit.symbol
        )

        if (!favorites.contains(favorite)) {

            favorites = (favorites + favorite).takeLast(5)

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

    fun setCategory(category: UnitCategory) {

        selectedCategory = category

        fromUnit = category.units.first()
        toUnit = category.units[1]

        input = ""
        result = ""
    }

    fun applyFavorite(fav: FavoriteConversion) {

        val fromResult =
            UnitRepository.findUnitAcrossCategories(fav.from)

        val toResult =
            UnitRepository.findUnitAcrossCategories(fav.to)

        if (fromResult != null && toResult != null) {

            val (category, fromUnitResult) = fromResult
            val toUnitResult = toResult.second

            // update category
            selectedCategory = category

            // update units
            fromUnit = fromUnitResult
            toUnit = toUnitResult

            convert()
        }
    }
}