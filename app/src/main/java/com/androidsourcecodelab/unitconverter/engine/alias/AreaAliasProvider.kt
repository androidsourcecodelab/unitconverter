package com.androidsourcecodelab.unitconverter.alias

import com.androidsourcecodelab.unitconverter.engine.alias.AliasProvider

class AreaAliasProvider : AliasProvider {

    private val aliases = mapOf(

        // Square meter
        "m2" to "m²",
        "sqm" to "m²",
        "squaremeter" to "m²",
        "sqmeter" to "m²",

        // Square kilometer
        "km2" to "km²",
        "sqkm" to "km²",
        "squarekilometer" to "km²",

        // Square centimeter
        "cm2" to "cm²",
        "sqcm" to "cm²",
        "squarecentimeter" to "cm²",

        // Square millimeter
        "mm2" to "mm²",
        "sqmm" to "mm²",
        "squaremillimeter" to "mm²",

        // Square foot
        "ft2" to "ft²",
        "sqft" to "ft²",
        "squarefoot" to "ft²",

        // Square yard
        "yd2" to "yd²",
        "sqyd" to "yd²",
        "squareyard" to "yd²",

        // Square inch
        "in2" to "in²",
        "sqin" to "in²",
        "squareinch" to "in²",

        // Acre
        "acre" to "acre",

        // Hectare
        "ha" to "ha",
        "hectare" to "ha"
    )

    override fun resolve(input: String): String? {
        val normalized = input
            .lowercase()
            .replace("²", "2")   // normalize unicode
            .replace("-", "")

        return aliases[normalized]
            ?: aliases[normalized.removeSuffix("s")]
    }

    override fun resolve(tokens: List<String>, index: Int): Pair<String, Int>? {

        if (index >= tokens.size - 1) return null

        val first = tokens[index].lowercase()
        val second = tokens[index + 1].lowercase()

        val combined = (first + second)
            .replace("-", "")
            .replace("²", "2")

        val resolved = aliases[combined]
            ?: aliases[combined.removeSuffix("s")]

        return resolved?.let { it to 2 }
    }
}