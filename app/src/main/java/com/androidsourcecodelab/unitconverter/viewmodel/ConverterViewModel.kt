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
import com.androidsourcecodelab.unitconverter.reference.ReferenceItem
import com.androidsourcecodelab.unitconverter.reference.findReference
import com.androidsourcecodelab.unitconverter.repository.categories.LengthCategory
import kotlinx.coroutines.launch
import kotlin.math.abs
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

    var reference by mutableStateOf<ReferenceItem?>(null)
        private set

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
        val category = selectedCategory;

        val value = input.toDoubleOrNull() ?: run {
            result = ""
            return
        }

        if (selectedCategory.type== ConverterType.TEMPERATURE) {
            val temp = convertTemperature(value, fromUnit.symbol, toUnit.symbol)
            result = formatNumber(temp)
            return
        }
        val base = value * fromUnit.factor

        reference = if (
            selectedCategory.name == LengthCategory.category.name &&
            fromUnit.symbol == "m"
        ) {
            findReference(base, LengthReferences.items)
        } else {
            null
        }
        val output = base / toUnit.factor

        result = formatNumber(output)
    }

    fun convertTemperature(value: Double, from: String, to: String): Double {

        val celsius = when (from) {
            "°C" -> value
            "°F" -> (value - 32) * 5 / 9
            "K" -> value - 273.15
            else -> value
        }

        return when (to) {
            "°C" -> celsius
            "°F" -> celsius * 9 / 5 + 32
            "K" -> celsius + 273.15
            else -> celsius
        }

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

        val magnitude = 10.0.pow(floor(log10(abs(value))))
        val step = magnitude / 10

        return listOf(
            value - 2 * step,
            value - step,
            value + step,
            value + 2 * step
        )
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