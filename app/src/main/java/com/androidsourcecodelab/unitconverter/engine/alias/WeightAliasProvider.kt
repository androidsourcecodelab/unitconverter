package com.androidsourcecodelab.unitconverter.alias

import com.androidsourcecodelab.unitconverter.engine.alias.AliasProvider

class WeightAliasProvider : AliasProvider {

    private val aliases = mapOf(
        "kg" to "kg",
        "kilogram" to "kg",

        "g" to "g",
        "gram" to "g",

        "lb" to "lb",
        "pound" to "lb",

        "oz" to "oz",
        "ounce" to "oz",

        "st" to "st",
        "stone" to "st",

        // single-token versions
        "uston" to "ton_us",
        "ukton" to "ton_uk",
        "metricton" to "t",
        "shortton" to "ton_us",
        "longton" to "ton_uk"
    )

    override fun resolve(input: String): String? {
        val lower = input.lowercase().replace("-", "")

        return aliases[lower]
            ?: aliases[lower.removeSuffix("s")]
    }

    override fun resolve(tokens: List<String>, index: Int): Pair<String, Int>? {

        if (index >= tokens.size - 1) return null

        val first = tokens[index]
        val second = tokens[index + 1]

        val combined = (first + second).replace("-", "").lowercase()

        val resolved = aliases[combined]
        if (resolved != null) {
            return resolved to 2   // consumed 2 tokens
        }

        return null
    }
}