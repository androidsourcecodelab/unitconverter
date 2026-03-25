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
import com.androidsourcecodelab.unitconverter.engine.preprocess.PreprocessResult
import com.androidsourcecodelab.unitconverter.engine.validation.ValidatorFactory
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.util.AliasResolver
import com.androidsourcecodelab.unitconverter.util.UnitAliasResolver
import com.androidsourcecodelab.unitconverter.util.UnitAliasResolver.parseConversion
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import com.androidsourcecodelab.unitconverter.engine.preprocess.PreprocessingPipeline
import com.androidsourcecodelab.unitconverter.engine.preprocess.CompositeCommand
import com.androidsourcecodelab.unitconverter.manager.CategoryManager.onCategoryUsed

class ConverterViewModel(application: Application) : ViewModel() {
    // ---------------------------
    // ✅ SINGLE SOURCE OF TRUTH
    // ---------------------------

    private val preprocessingPipeline = PreprocessingPipeline(
        UnitRepository.allCategories
            .filter { it.supportsComposite }
            .map { CompositeCommand(it) }
    )



    private fun detectCategoryFromInput(input: String): UnitCategory? {

        if (!input.contains("to")) return null   // only composite

        val tokens = input.lowercase().split(Regex("\\s+"))

        val matchedCategories = mutableSetOf<UnitCategory>()

        var i = 0

        while (i < tokens.size) {

            val resolved = AliasResolver.normalize(tokens, i)

            if (resolved != null) {
                val (symbol, consumed) = resolved
                val category = UnitRepository.getCategoryByUnitSymbol(symbol)
                if (category != null) {
                    matchedCategories.add(category)
                }

                i += consumed
            } else {
                i++
            }
        }

        return if (matchedCategories.size == 1) {
            matchedCategories.first()
        } else {
            null
        }
    }
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
        MANUAL,
        PARTIAL_NLP,
        EMPTY
    }

    fun extractFromUnit(input: String): Pair<String, UnitItem>? {

        val tokens = input.trim().lowercase().split("\\s+".toRegex())

        if (tokens.size < 2) return null

        val unitToken = tokens[1]

        val normalized = AliasResolver.normalize(listOf(unitToken), 0)?.first
            ?: return null

        val pair = UnitRepository.findUnitAcrossCategories(normalized)
            ?: return null

        return normalized to pair.second
    }

    fun getSuggestions(fromUnit: UnitItem, category: UnitCategory): List<UnitItem> {

        return category.units
            .filter { it.symbol != fromUnit.symbol }
            .take(3)   // 🔥 limit
    }

    fun classifyInput(input: String): InputMode {

        val tokens = input.trim().lowercase().split("\\s+".toRegex())

        return when {

            input.isBlank() -> InputMode.EMPTY

            // 🔥 "10 km"
            tokens.size == 2 && tokens[0].toDoubleOrNull() != null -> {
                InputMode.PARTIAL_NLP
            }

            // 🔥 any presence of "to" as a token → NLP (complete or incomplete)
            tokens.contains("to") -> {
                InputMode.NLP
            }

            else -> InputMode.MANUAL
        }
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
            rawInputText = text,
            errorMessage = null)
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
            errorMessage = "",
            isComposite = false,
            compositeComponents = emptyList()
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
            parsedCommand = null,
            compositeComponents = emptyList(),
            isComposite = false
        )
    }

    fun clearResultOnly() {
        state = state.copy(
            result = "",
            errorMessage = null
        )
    }

    fun applySuggestion(toUnit: UnitItem) {

        val tokens = state.rawInputText.trim().split("\\s+".toRegex())

        if (tokens.size < 2) return

        val value = tokens[0]

        // 🔥 ALWAYS extract from input (not state)
        val extracted = extractFromUnit(state.rawInputText) ?: return
        val fromUnit = extracted.second

        val newInput = "$value ${fromUnit.symbol} to ${toUnit.symbol}"

        state = state.copy(
            rawInputText = newInput,
            suggestions = emptyList(),
            parsedCommand = null,
            isComposite = false,
            compositeComponents = emptyList()
        )

        onConvert()
    }




    // ---------------------------
    // 🔄 CONVERT (ONLY COMPUTE)
    // ---------------------------
    fun onConvert() {

        val input = state.rawInputText.trim()

// 🔥 NEW: Preprocessing pipeline

        val detectedCategory = detectCategoryFromInput(input)

        val effectiveCategory =
            if (detectedCategory?.supportsComposite == true) {
                detectedCategory
            } else {
                null
            }

        val preprocessResult =
            if (effectiveCategory != null) {
                preprocessingPipeline.execute(input, effectiveCategory)
            } else {
                PreprocessResult.NotApplicable
            }
        when (preprocessResult) {

            is PreprocessResult.Success -> {

                val category = preprocessResult.category

                if (category != null) {
                    onCategoryUsed(category)   // 🔥 THIS is the missing link
                }

                val fromUnitItem = UnitRepository.getUnitItem(category, preprocessResult.fromUnit)
                val toUnitItem = preprocessResult.toUnit
                    ?.let { UnitRepository.getUnitItem(category, it) }

                // 🔥 STEP 1: update FULL state context FIRST
                state = state.copy(
                    category = category,
                    categories = CategoryManager.getVisibleCategories(),
                    fromUnit = fromUnitItem,
                    toUnit = toUnitItem ?: fromUnitItem,
                    compositeComponents = preprocessResult.components,
                    isComposite = true
                )

                // 🔥 STEP 2: run conversion
                val (result, error) = handleManualOverride(
                    value = preprocessResult.value,
                    fromUnitSymbol = preprocessResult.fromUnit,
                    toUnitOverride = preprocessResult.toUnit
                )

                // 🔥 STEP 3: update result
                state = state.copy(
                    parsedValue = preprocessResult.value.toString(),
                    result = result,
                    errorMessage = error,
                    suggestions = emptyList(),
                    parsedCommand = null
                )

                return
            }

            is PreprocessResult.Failure -> {
                state = state.copy(
                    errorMessage = preprocessResult.message
                )
                return
            }

            PreprocessResult.NotApplicable -> {
                // continue normal flow
            }
        }

        val mode = classifyInput(input)

        Log.d("InputMode", "Detected mode: $mode")

        // ---------------------------
        // 1. EMPTY
        // ---------------------------
        if (mode == InputMode.EMPTY) {
            state = state.copy(
                parsedValue = "",
                parsedCommand = null,
                result = "",
                errorMessage = null,
                suggestions = emptyList(),
                isComposite = false,
                compositeComponents = emptyList()
            )
            return
        }

        if (mode == InputMode.PARTIAL_NLP) {

            val tokens = input.trim().split("\\s+".toRegex())

            // 🔥 Safety check
            if (tokens.size < 2) {
                state = state.copy(
                    result = "",
                    parsedCommand = null,
                    suggestions = emptyList(),
                    errorMessage = "Invalid input",
                    isComposite = false,
                    compositeComponents = emptyList()
                )
            }

            val valueToken = tokens[0]
            val unitToken = tokens[1]

            val value = valueToken.toDoubleOrNull()

            // ❌ If value itself invalid → let manual handle later
            if (value == null) {
                state = state.copy(
                    result = "",
                    parsedCommand = null,
                    suggestions = emptyList(),
                    errorMessage = "Invalid input",
                    isComposite = false,
                    compositeComponents = emptyList()
                )
            }

            val extracted = extractFromUnit(input)

            // ❌ Case: invalid unit like "xxx"
            if (extracted == null) {

                state = state.copy(
                    result = "",
                    parsedCommand = null,
                    suggestions = emptyList(),
                    errorMessage = "Unknown unit: $unitToken",
                    isComposite = false,
                    compositeComponents = emptyList()
                )

                return
            }

            else {

                val (normalizedSymbol, fromUnit) = extracted
                val detectedCategory = UnitRepository.findCategoryForUnit(fromUnit)

                if (detectedCategory == null) {

                    val unitToken = input.substringAfter(" ", "").trim()

                    state = state.copy(
                        result = "",
                        parsedCommand = null,
                        suggestions = emptyList(),
                        errorMessage = "Unknown unit: $unitToken",
                        isComposite = false,
                        compositeComponents = emptyList()
                    )

                    return
                }

                // ✅ Case 1: Category matches
                if (detectedCategory == state.category) {

                    val toUnit = state.toUnit

                    // 🔁 Case 1A: Same unit → suggestions instead of useless conversion
                    if (toUnit != null && toUnit.symbol == fromUnit.symbol) {

                        val suggestions = getSuggestions(fromUnit, detectedCategory)

                        state = state.copy(
                            suggestions = suggestions,
                            result = "",
                            parsedCommand = null,
                            errorMessage = null,
                            isComposite = false,
                            compositeComponents = emptyList()
                        )

                        return
                    }

                    // ✅ Extract numeric value safely
                    val value = input.substringBefore(" ").toDoubleOrNull()
                        ?: return

                    // 🔥 IMPORTANT: update state BEFORE calling handleManual
                    state = state.copy(
                        fromUnit = fromUnit,
                        rawInputText = value.toString(),
                        parsedValue = value.toString(),
                        suggestions = emptyList(),
                        parsedCommand = null,
                        isComposite = false,
                        compositeComponents = emptyList()
                    )

                    // ✅ Reuse manual pipeline
                    val (result, error) = handleManual(value.toString())

                    state = state.copy(
                        result = result,
                        errorMessage = error
                    )

                    return
                }

                // ❗ Case 2: Category mismatch → suggestions
                val suggestions = getSuggestions(fromUnit, detectedCategory)

                state = state.copy(
                    suggestions = suggestions,
                    result = "",
                    parsedCommand = null,
                    errorMessage = null,
                    isComposite = false,
                    compositeComponents = emptyList()
                )
            }

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
                        errorMessage = error,
                        suggestions = emptyList()
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
            errorMessage = error,
            suggestions = emptyList()
        )
    }

    fun clearInput() {
        state = state.copy(
            rawInputText = "",
            parsedValue = "",
            parsedCommand = null,
            result = "",
            errorMessage = "",
            compositeComponents = emptyList(),
            isComposite = false

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

    private fun handleManualOverride(
        value: Double,
        fromUnitSymbol: String,
        toUnitOverride: String?
    ): Pair<String, String?> {

        val category = state.category ?: return "" to null

        // 🔥 Ensure target unit is a proper String
        val toRaw: String = (toUnitOverride ?: state.toUnit) as? String
            ?: return "" to "Target unit not selected"

        val fromRaw: String = fromUnitSymbol

        // 🔥 Normalize using AliasResolver (safe usage)
        val normalizedFromUnit = AliasResolver
            .normalize(listOf(fromRaw), 0)
            ?.first
            ?: fromRaw

        val normalizedToUnit = AliasResolver
            .normalize(listOf(toRaw), 0)
            ?.first
            ?: toRaw

        // 🔥 Resolve to UnitItem
        val fromUnitItem = UnitRepository.getUnitItem(category, normalizedFromUnit)
            ?: return "" to "Invalid source unit"

        val toUnitItem = UnitRepository.getUnitItem(category, normalizedToUnit)
            ?: return "" to "Invalid target unit"

        val input = value.toString()

        val validator = ValidatorFactory.get(category)
        val validation = validator.validate(input, fromUnitItem)

        if (!validation.isValid) {
            return "" to validation.errorMessage
        }

        val strategy = ConversionStrategyFactory.getStrategy(category)
        val formatter = FormatStrategyFactory.get(category)

        val resultValue = strategy.convert(
            input,
            fromUnitItem,
            toUnitItem
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