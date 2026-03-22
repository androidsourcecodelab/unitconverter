package com.androidsourcecodelab.unitconverter.util

import com.androidsourcecodelab.unitconverter.data.UnitRepository
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object UnitAliasResolver {


    data class ParsedCommand(
        val rawInput: String,
        val value: String,          // 🔥 single source of truth
        val fromUnit: UnitItem,
        val toUnit: UnitItem,
        val category: UnitCategory
    )

    sealed class ParseResult {
        data class Success(val command: ParsedCommand) : ParseResult()
        data class Error(val message: String) : ParseResult()
    }


    fun parseConversion(input: String): ParseResult {

        if (input.isBlank()) {
            return ParseResult.Error("Input cannot be empty")
        }

        val tokens = input.trim().lowercase().split("\\s+".toRegex())

        if (tokens.size < 4) {
            return ParseResult.Error("Invalid format. Use: value unit to unit")
        }

        val valueToken = tokens[0]

        val toIndex = tokens.indexOf("to")
        if (toIndex == -1 || toIndex < 2 || toIndex >= tokens.size - 1) {
            return ParseResult.Error("Missing or incorrect 'to' keyword")
        }

        // ---------------------------
        // FROM UNIT (multi-token aware)
        // ---------------------------
        val fromResolved = AliasResolver.normalize(tokens, 1)
            ?: return ParseResult.Error("Invalid source unit")

        val (fromToken, consumed1) = fromResolved

        // ---------------------------
        // TO UNIT (multi-token aware)
        // ---------------------------
        val toStartIndex = 1 + consumed1 + 1

        if (toStartIndex >= tokens.size) {
            return ParseResult.Error("Invalid target unit")
        }

        val toResolved = AliasResolver.normalize(tokens, toStartIndex)
            ?: return ParseResult.Error("Invalid target unit")

        val (toToken, _) = toResolved

        // ---------------------------
        // LOOKUP
        // ---------------------------
        val fromPair = UnitRepository.findUnitAcrossCategories(fromToken)
            ?: return ParseResult.Error("Unknown unit: ${tokens[1]}")

        val toPair = UnitRepository.findUnitAcrossCategories(toToken)
            ?: return ParseResult.Error("Unknown unit: ${tokens[toStartIndex]}")

        val fromCategory = fromPair.first
        val toCategory = toPair.first

        if (fromCategory != toCategory) {
            return ParseResult.Error(
                "Cannot convert ${fromCategory.name} to ${toCategory.name}"
            )
        }

        // ---------------------------
        // SUCCESS
        // ---------------------------
        return ParseResult.Success(
            ParsedCommand(
                rawInput = input,
                value = valueToken.uppercase(),
                fromUnit = fromPair.second,
                toUnit = toPair.second,
                category = fromCategory
            )
        )
    }






}