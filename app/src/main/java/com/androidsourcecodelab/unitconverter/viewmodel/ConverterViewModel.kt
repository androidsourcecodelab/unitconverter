package com.androidsourcecodelab.unitconverter.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidsourcecodelab.unitconverter.FavoriteConversion
import com.androidsourcecodelab.unitconverter.data.FavoritesRepository
import com.androidsourcecodelab.unitconverter.data.UnitRepository
import com.androidsourcecodelab.unitconverter.engine.ConversionStrategyFactory
import com.androidsourcecodelab.unitconverter.engine.format.FormatStrategyFactory
import com.androidsourcecodelab.unitconverter.engine.validation.ValidatorFactory
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.reference.ReferenceItem
import com.androidsourcecodelab.unitconverter.util.UnitAliasResolver
import com.androidsourcecodelab.unitconverter.util.UnitAliasResolver.parseConversion
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class ConverterViewModel(application: Application) : ViewModel() {
    // ---------------------------
    // ✅ SINGLE SOURCE OF TRUTH
    // ---------------------------

    var state by mutableStateOf(UiState())
        private set

    init {
        val defaultCategory = UnitRepository.categories.first()

        state = state.copy(
            category = defaultCategory,
            fromUnit = defaultCategory.units.firstOrNull(),
            toUnit = defaultCategory.units.getOrNull(1)
        )
    }

    fun onInputChanged(text: String) {
        state = state.copy(
            rawInputText = text)
    }

    fun onToUnitChanged(newTo: UnitItem) {

        val currentFrom = state.fromUnit

        val (from, to) = if (newTo == currentFrom) {
            // 🔥 swap case
            currentFrom to state.toUnit
        } else {
            currentFrom to newTo
        }

        state = state.copy(
            fromUnit = from,
            toUnit = to,
            rawInputText = "",
            parsedValue = "",
            parsedCommand = null,
            result = "",
            errorMessage = ""
        )
    }


    fun onFromUnitChanged(newFrom: UnitItem) {

        val currentTo = state.toUnit

        val (from, to) = if (newFrom == currentTo) {
            // 🔥 explicit swap logic
            currentTo to state.fromUnit
        } else {
            newFrom to currentTo
        }

        state = state.copy(
            fromUnit = from,
            toUnit = to,
            rawInputText = "",
            // 🔥 reset
            result = "",
            parsedCommand = null
        )
    }

    fun clearResultOnly() {
        state = state.copy(
            result = "",
            errorMessage = null
        )
    }

    // ---------------------------
    // 🔄 CONVERT (ONLY COMPUTE)
    // ---------------------------
    fun onConvert() {

        val input = state.rawInputText.trim()

        // ---------------------------
        // 1. EMPTY
        // ---------------------------
        if (input.isEmpty()) {
            state = state.copy(
                parsedValue = "",
                parsedCommand = null,
                result = "",
                errorMessage = null   // 🔥 clear error
            )
            return
        }

        // ---------------------------
        // 2. TRY NLP
        // ---------------------------
        val parsed = parseConversion(input)

        if (parsed != null) {

            val (result, error) = handleNlp(parsed)

            state = state.copy(
                parsedCommand = parsed,
                parsedValue = parsed.value,
                category = parsed.category,
                fromUnit = parsed.fromUnit,
                toUnit = parsed.toUnit,
                result = result,
                errorMessage = error   // 🔥 set error (null if valid)
            )

            return
        }

        // ---------------------------
        // 3. MANUAL
        // ---------------------------
        val (result, error) = handleManual(input)

        state = state.copy(
            parsedCommand = null,
            parsedValue = input,
            result = result,
            errorMessage = error   // 🔥 set error
        )
    }
    // ---------------------------
    // 🔁 STRUCTURAL CHANGE
    // (swap, category, favorites)
    // ---------------------------
    fun onStructureChanged(
        category: UnitCategory? = state.category,
        from: UnitItem? = state.fromUnit,
        to: UnitItem? = state.toUnit,

    ) {
        state = state.copy(
            category = category,
            fromUnit = from,
            toUnit = to,
            rawInputText = "",     // 🔥 updated field name
            parsedValue = "",      // 🔥 reset
            parsedCommand = null,  // 🔥 important (clear NLP state)
            result = ""            // 🔥 reset
        )
    }

    fun clearInput() {
        state = state.copy(
            rawInputText = "",
            parsedValue = "",
            parsedCommand = null,
            result = "",
            errorMessage = ""
        )
    }

    // ---------------------------
    // 🔁 SWAP (CLEAN + EXPLICIT)
    // ---------------------------
    fun onSwap() {
        state = state.copy(
            fromUnit = state.toUnit,
            toUnit = state.fromUnit,
            rawInputText = "",     // 🔥 updated
            parsedValue = "",      // 🔥 reset
            parsedCommand = null,  // 🔥 reset NLP state
            result = ""            // 🔥 reset
        )
    }


    private fun handleNlp(parsed: UnitAliasResolver.ParsedCommand): Pair<String, String?> {

        val validator = ValidatorFactory.get(parsed.category)
        val validation = validator.validate(parsed.value, parsed.fromUnit)

        if (!validation.isValid) {
            return "" to validation.errorMessage
        }

        val strategy = ConversionStrategyFactory.getStrategy(parsed.category)
        val formatter = FormatStrategyFactory.get(parsed.category)

        val resultValue = strategy.convert(
            parsed.value,
            parsed.fromUnit,
            parsed.toUnit
        )

        return formatter.format(resultValue) to null
    }
    // ---------------------------
    // 🔢 MANUAL HANDLER
    // ---------------------------
    private fun handleManual(input: String): Pair<String, String?> {

        val category = state.category ?: return "" to null
        val from = state.fromUnit ?: return "" to null
        val to = state.toUnit ?: return "" to null

        val validator = ValidatorFactory.get(category)
        val validation = validator.validate(input, from)

        if (!validation.isValid) {
            return "" to validation.errorMessage
        }

        val strategy = ConversionStrategyFactory.getStrategy(category)
        val formatter = FormatStrategyFactory.get(category)

        val resultValue = strategy.convert(input, from, to)

        return formatter.format(resultValue) to null
    }

    val categories = UnitRepository.categories
    private val favoritesRepository =
        FavoritesRepository(application)
    var favorites by mutableStateOf(listOf<FavoriteConversion>())
    //var favorites = mutableStateListOf<Favorite>()
    public val formatter = DecimalFormat("#,###.####")
    private val scientificFormatter =
        DecimalFormat("0.###E0")

    var inputText by mutableStateOf("")
    var inputValue by mutableStateOf("") // numeric part

    var input by mutableStateOf("")
    var result by mutableStateOf("")

    var reference by mutableStateOf<ReferenceItem?>(null)
        private set

    var isNlpMode by mutableStateOf(false)

    var selectedCategory by mutableStateOf(UnitRepository.categories.first())

    var fromUnit by mutableStateOf(selectedCategory.units.first())
    var toUnit by mutableStateOf(selectedCategory.units[1])

    init {

        viewModelScope.launch {

            favorites =
                favoritesRepository.loadFavorites()

        }
    }


    fun swapUnits() {

        val currentFrom = state.fromUnit
        val currentTo = state.toUnit

        state = state.copy(
            fromUnit = currentTo,
            toUnit = currentFrom,
            rawInputText = "",     // 🔥 reset
            parsedValue = "",      // 🔥 reset
            parsedCommand = null,  // 🔥 reset NLP
            result = "" ,
            errorMessage = ""// 🔥 reset
        )
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

        val from = state.fromUnit ?: return
        val to = state.toUnit ?: return

        Log.d("FavoritesRepo adding", "${from.symbol},${to.symbol}")

        if (favorites.size >= 5) return

        val favorite = FavoriteConversion(
            from.symbol,
            to.symbol
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

        val from = category.units.firstOrNull()
        val to = category.units.getOrNull(1)

        state = state.copy(
            category = category,
            fromUnit = from,
            toUnit = to,
            rawInputText = "",
            parsedValue = "",        // 🔥 reset input
            result = "",           // 🔥 reset result
            parsedCommand = null   // 🔥 clear NLP state
        )
    }

    fun applyFavorite(fav: FavoriteConversion) {

        val fromResult = UnitRepository.findUnitAcrossCategories(fav.from)
        val toResult = UnitRepository.findUnitAcrossCategories(fav.to)

        if (fromResult != null && toResult != null) {

            val (category, fromUnitResult) = fromResult
            val toUnitResult = toResult.second

            state = state.copy(
                category = category,
                fromUnit = fromUnitResult,
                toUnit = toUnitResult,
                rawInputText = "",     // 🔥 reset
                parsedValue = "",      // 🔥 reset
                parsedCommand = null,  // 🔥 reset
                result = ""            // 🔥 reset
            )
        }
    }


    /*fun convertUnified(input: Any) {

        try {

            val strategy = ConversionStrategyFactory.getStrategy(selectedCategory)

            result = strategy.convert(
                input,
                fromUnit,
                toUnit
            )

        } catch (e: Exception) {
            result = "Error"
        }
    }*/
}