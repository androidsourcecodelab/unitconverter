package com.androidsourcecodelab.unitconverter.util

import com.androidsourcecodelab.unitconverter.data.UnitRepository
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object UnitAliasResolver {

    private val aliases = mapOf(

        // Length
        "km" to "km",
        "kms" to "km",
        "kilometer" to "km",
        "kilometers" to "km",
        "kilometre" to "km",
        "kilometres" to "km",

        "m" to "m",
        "meter" to "m",
        "meters" to "m",

        "nm" to "nm",
        "nanometer" to "nm",
        "nanometers" to "nm",

        "mi" to "mi",
        "mile" to "mi",
        "miles" to "mi",

        // Mass
        "kg" to "kg",
        "kgs" to "kg",
        "kilogram" to "kg",
        "kilograms" to "kg",

        "lb" to "lb",
        "pound" to "lb",
        "pounds" to "lb",

        "bin" to "BIN",
        "binary" to "BIN",


        // number base

        "oct" to "OCT",
        "octal" to "OCT",

        "dec" to "DEC",
        "decimal" to "DEC",

        "hex" to "HEX",
        "hexadecimal" to "HEX",
    )

    fun parseInput(text: String): Pair<String, String?> {

        val cleaned = text.trim().lowercase()

        val parts = cleaned.split("\\s+".toRegex())

        val numberPart = parts.getOrNull(0) ?: ""
        val unitPart = parts.getOrNull(1)

        val resolved = unitPart?.let { aliases[it] }

        return Pair(numberPart, resolved)
    }



    data class ParsedCommand(
        val rawInput: String,
        val value: String,          // 🔥 single source of truth
        val fromUnit: UnitItem,
        val toUnit: UnitItem,
        val category: UnitCategory
    )

    fun parseConversion(input: String): ParsedCommand? {

        if (input.isBlank()) return null

        val tokens = input.trim().lowercase().split("\\s+".toRegex())

        if (tokens.size < 4) return null

        val valueToken = tokens[0]

        val toIndex = tokens.indexOf("to")
        if (toIndex == -1 || toIndex < 2 || toIndex >= tokens.size - 1) return null

        val fromTokenRaw = tokens[1]
        val toTokenRaw = tokens[toIndex + 1]

        val fromToken = UnitAliasResolver.normalize(fromTokenRaw) ?: return null
        val toToken = UnitAliasResolver.normalize(toTokenRaw) ?: return null

        val fromPair = UnitRepository.findUnitAcrossCategories(fromToken)
        val toPair = UnitRepository.findUnitAcrossCategories(toToken)

        if (fromPair == null || toPair == null) return null

        val fromCategory = fromPair.first
        val toCategory = toPair.first

        if (fromCategory != toCategory) return null

        return ParsedCommand(
            rawInput = input,
            value = valueToken.uppercase(),
            fromUnit = fromPair.second,
            toUnit = toPair.second,
            category = fromCategory
        )
    }

    fun normalize(word: String?): String? {
        if (word == null) return null

        val lower = word.lowercase()

        return aliases[lower]
            ?: aliases[lower.removeSuffix("s")]
            ?: lower
    }




}