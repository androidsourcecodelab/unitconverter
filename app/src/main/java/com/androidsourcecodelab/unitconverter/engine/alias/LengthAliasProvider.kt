package com.androidsourcecodelab.unitconverter.alias

import com.androidsourcecodelab.unitconverter.engine.alias.AliasProvider

class LengthAliasProvider : AliasProvider {

    private val aliases = mapOf(

        // 🔹 Meter
        "m" to "m",
        "meter" to "m",
        "meters" to "m",
        "metre" to "m",
        "metres" to "m",

        // 🔹 Kilometer
        "km" to "km",
        "kilometer" to "km",
        "kilometers" to "km",
        "kilometre" to "km",
        "kilometres" to "km",

        // 🔹 Centimeter
        "cm" to "cm",
        "centimeter" to "cm",
        "centimeters" to "cm",
        "centimetre" to "cm",
        "centimetres" to "cm",

        // 🔹 Millimeter
        "mm" to "mm",
        "millimeter" to "mm",
        "millimeters" to "mm",
        "millimetre" to "mm",
        "millimetres" to "mm",

        // 🔹 Micrometer
        "µm" to "µm",
        "um" to "µm",
        "micrometer" to "µm",
        "micrometers" to "µm",
        "micrometre" to "µm",
        "micrometres" to "µm",

        // 🔹 Nanometer
        "nm" to "nm",
        "nanometer" to "nm",
        "nanometers" to "nm",
        "nanometre" to "nm",
        "nanometres" to "nm",

        // 🔹 Mile
        "mi" to "mi",
        "mile" to "mi",
        "miles" to "mi",

        // 🔹 Yard
        "yd" to "yd",
        "yard" to "yd",
        "yards" to "yd",

        // 🔹 Foot
        "ft" to "ft",
        "foot" to "ft",
        "feet" to "ft",

        // 🔹 Inch
        "in" to "in",
        "inch" to "in",
        "inches" to "in",

        // 🔹 Nautical Mile
        "nmi" to "nmi",
        "nauticalmile" to "nmi",
        "nauticalmiles" to "nmi",

        // 🔹 Astronomical Unit
        "au" to "AU",
        "astronomicalunit" to "AU",
        "astronomicalunits" to "AU",

        // 🔹 Light Year
        "ly" to "ly",
        "lightyear" to "ly",
        "lightyears" to "ly",

        // 🔹 Parsec
        "pc" to "pc",
        "parsec" to "pc",
        "parsecs" to "pc"
    )

    override fun resolve(input: String): String? {
        val normalized = input
            .lowercase()
            .replace("-", "")
            .replace(" ", "")

        return aliases[normalized]
    }

    override fun resolve(
        tokens: List<String>,
        index: Int
    ): Pair<String, Int>? {

        if (index >= tokens.size) return null

        val first = tokens[index]
            .lowercase()
            .replace("-", "")
            .replace(" ", "")

        // 🔹 Try 2-token match
        if (index + 1 < tokens.size) {

            val second = tokens[index + 1]
                .lowercase()
                .replace("-", "")
                .replace(" ", "")

            val combined = first + second

            val resolved = aliases[combined]
            if (resolved != null) {
                return resolved to 2
            }
        }

        // 🔹 Fallback: single token
        val resolvedSingle = aliases[first]

        if (resolvedSingle != null) {
            return resolvedSingle to 1
        }

        return null
    }
}