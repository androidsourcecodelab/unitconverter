package com.androidsourcecodelab.unitconverter.viewmodel

import android.app.Application
import com.androidsourcecodelab.unitconverter.manager.CategoryManager
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
import com.androidsourcecodelab.unitconverter.util.UnitAliasResolver
import com.androidsourcecodelab.unitconverter.util.UnitAliasResolver.parseConversion
import kotlinx.coroutines.launch
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
        val defaultCategory = UnitRepository.defaultCategories.first()

        state = state.copy(
            category = defaultCategory,
            fromUnit = defaultCategory.units.firstOrNull(),
            toUnit = defaultCategory.units.getOrNull(1)
        )
    }

    enum class InputMode {
        NLP,
        MANUAL
    }

    fun classifyInput(input: String): InputMode {
        val normalized = input.lowercase()

        return if (normalized.contains("to")) InputMode.NLP
        else InputMode.MANUAL
    }

    fun detectNlpHint(input: String): String? {
        val normalized = input.trim().lowercase()

        // Case 1: ends with "to"
        if (normalized.endsWith(" to")) {
            return "Enter target unit after 'to'"
        }

        // Case 2: contains 'to' but incomplete
        if (normalized.contains(" to ")) {
            val tokens = normalized.split("\\s+".toRegex())
            val toIndex = tokens.indexOf("to")

            if (toIndex == tokens.size - 1) {
                return "Enter target unit after 'to'"
            }
        }

        // Case 3: looks like NLP but incomplete
        val tokens = normalized.split("\\s+".toRegex())
        if (tokens.size >= 2 && !normalized.contains(" to ")) {
            return "Use format: value unit to unit (e.g. 10 km to m)"
        }

        return null
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
        val mode = classifyInput(input)

        Log.d("InputMode", "Detected mode: $mode")

        // ---------------------------
        // 1. EMPTY
        // ---------------------------
        if (input.isEmpty()) {
            state = state.copy(
                parsedValue = "",
                parsedCommand = null,
                result = "",
                errorMessage = null
            )
            return
        }

        // ---------------------------
        // 2. NLP MODE (EXPLICIT)
        // ---------------------------
        if (mode == InputMode.NLP) {

            when (val result = parseConversion(input)) {

                is UnitAliasResolver.ParseResult.Success -> {
                    val command = result.command
                    // 🔥 CRITICAL LINE
                    CategoryManager.onCategoryUsed(command.category)

                    val (output, error) = handleNlp(command)

                    state = state.copy(
                        categories = CategoryManager.getVisibleCategories(),
                        parsedCommand = command,
                        parsedValue = command.value,
                        category = command.category,
                        fromUnit = command.fromUnit,
                        toUnit = command.toUnit,
                        result = output,
                        errorMessage = error
                    )
                }

                is UnitAliasResolver.ParseResult.Error -> {

                    val hint = detectNlpHint(input)

                    state = state.copy(
                        parsedCommand = null,
                        result = "",
                        errorMessage = hint ?: result.message
                    )
                }
            }

            return
        }
        // ---------------------------
        // 3. MANUAL MODE
        // ---------------------------
        val (result, error) = handleManual(input)

        state = state.copy(
            parsedCommand = null,
            parsedValue = input,
            result = result,
            errorMessage = error
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

    private val favoritesRepository =
        FavoritesRepository(application)
    var favorites by mutableStateOf(listOf<FavoriteConversion>())


    init {

        viewModelScope.launch {

            favorites =
                favoritesRepository.loadFavorites()

        }

        val categories = CategoryManager.getVisibleCategories()

        val defaultCategory = categories.firstOrNull()
            ?: UnitRepository.defaultCategories.first()

        state = state.copy(
            categories = categories,   // 🔥 important
            category = defaultCategory,
            fromUnit = defaultCategory.units.firstOrNull(),
            toUnit = defaultCategory.units.getOrNull(1)
        )
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

        // 🔥 LRU update
        CategoryManager.onCategoryUsed(category)

        val from = category.units.firstOrNull()
        val to = category.units.getOrNull(1)

        state = state.copy(
            categories = CategoryManager.getVisibleCategories(), // 🔥 refresh
            category = category,
            fromUnit = from,
            toUnit = to,
            rawInputText = "",
            parsedValue = "",
            result = "",
            parsedCommand = null
        )
    }

    fun applyFavorite(fav: FavoriteConversion) {

        val fromResult = UnitRepository.findUnitAcrossCategories(fav.from)
        val toResult = UnitRepository.findUnitAcrossCategories(fav.to)

        if (fromResult != null && toResult != null) {

            val (category, fromUnitResult) = fromResult
            val toUnitResult = toResult.second

            // 🔥 1. Update LRU
            CategoryManager.onCategoryUsed(category)

            // 🔥 2. Update state INCLUDING categories
            state = state.copy(
                categories = CategoryManager.getVisibleCategories(),   // 🔥 important
                category = category,
                fromUnit = fromUnitResult,
                toUnit = toUnitResult,
                rawInputText = "",
                parsedValue = "",
                parsedCommand = null,
                result = ""
            )
        }
    }



}