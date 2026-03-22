package com.androidsourcecodelab.unitconverter.engine.alias

class PowerAliasProvider : AliasProvider {

    private val aliases = mapOf(

        // Watt
        "w" to "W",
        "watt" to "W",

        // Kilowatt
        "kw" to "kW",
        "kilowatt" to "kW",

        // Megawatt
        "mw" to "MW",
        "megawatt" to "MW",

        // Gigawatt
        "gw" to "GW",
        "gigawatt" to "GW",

        // Milliwatt
        "milliwatt" to "mW",

        // Microwatt
        "microwatt" to "µW",
        "uw" to "µW",

        // Horsepower
        "hp" to "hp",
        "horsepower" to "hp",

        // Mechanical horsepower
        "hpmech" to "hp_mech",
        "mechanicalhorsepower" to "hp_mech",

        // BTU/h
        "btuh" to "BTU/h",
        "btuperhour" to "BTU/h"
    )

    override fun resolve(input: String): String? {

        val normalized = input
            .lowercase()
            .replace("-", "")
            .replace(" ", "")

        return aliases[normalized]
            ?: aliases[normalized.removeSuffix("s")]
    }

    override fun resolve(tokens: List<String>, index: Int): Pair<String, Int>? {

        if (index >= tokens.size - 2) return null

        val first = tokens[index]
        val second = tokens[index + 1]
        val third = tokens[index + 2]

        // "btu per hour"
        if (second == "per") {
            val combined = (first + second + third)
                .lowercase()
                .replace("-", "")
                .replace(" ", "")

            val resolved = aliases[combined]
            if (resolved != null) {
                return resolved to 3
            }
        }

        return null
    }
}