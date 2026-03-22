package com.androidsourcecodelab.unitconverter.alias

import com.androidsourcecodelab.unitconverter.engine.alias.AliasProvider

class VolumeAliasProvider : AliasProvider {

    private val aliases = mapOf(

        // Cubic meter
        "m3" to "m³",
        "cubicmeter" to "m³",

        // Cubic kilometer
        "km3" to "km³",
        "cubickilometer" to "km³",

        // Cubic centimeter
        "cm3" to "cm³",
        "cubiccentimeter" to "cm³",

        // Cubic millimeter
        "mm3" to "mm³",
        "cubicmillimeter" to "mm³",

        // Liter
        "l" to "L",
        "liter" to "L",
        "litre" to "L",

        // Milliliter
        "ml" to "mL",
        "milliliter" to "mL",
        "millilitre" to "mL",

        // Cubic foot
        "ft3" to "ft³",
        "cubicfoot" to "ft³",

        // Cubic yard
        "yd3" to "yd³",
        "cubicyard" to "yd³",

        // Cubic inch
        "in3" to "in³",
        "cubicinch" to "in³",

        // US Gallon
        "galus" to "gal_us",
        "usgallon" to "gal_us",
        "gallonus" to "gal_us",

        // UK Gallon
        "galimp" to "gal_imp",
        "ukgallon" to "gal_imp",
        "gallonuk" to "gal_imp",

        // US Quart
        "qtus" to "qt_us",
        "usquart" to "qt_us",

        // US Pint
        "ptus" to "pt_us",
        "uspint" to "pt_us",

        // US Cup
        "cupus" to "cup_us",
        "uscup" to "cup_us"
    )

    override fun resolve(input: String): String? {
        val normalized = input
            .lowercase()
            .replace("³", "3")
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
            .replace("³", "3")

        val resolved = aliases[combined]
            ?: aliases[combined.removeSuffix("s")]

        return resolved?.let { it to 2 }
    }
}