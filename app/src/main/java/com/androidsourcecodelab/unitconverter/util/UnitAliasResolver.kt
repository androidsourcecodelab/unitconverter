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


    fun parseConversion(input: String): ParsedCommand? {

        if (input.isBlank()) return null

        val tokens = input.trim().lowercase().split("\\s+".toRegex())

        if (tokens.size < 4) return null

        val valueToken = tokens[0]

        val toIndex = tokens.indexOf("to")
        if (toIndex == -1 || toIndex < 2 || toIndex >= tokens.size - 1) return null



        //val fromToken = UnitAliasResolver.normalize(fromTokenRaw) ?: return null
        val (fromToken, consumed1) = AliasResolver.normalize(tokens, 1) ?: return null

        val toStartIndex = 1 + consumed1 + 1 // skip "to"

        val (toToken, _) = AliasResolver.normalize(tokens, toStartIndex) ?: return null
        //val toToken = UnitAliasResolver.normalize(toTokenRaw) ?: return null

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






}