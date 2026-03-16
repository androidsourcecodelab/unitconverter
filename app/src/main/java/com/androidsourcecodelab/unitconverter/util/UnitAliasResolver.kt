package com.androidsourcecodelab.unitconverter.util

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

        "mi" to "mi",
        "mile" to "mi",
        "miles" to "mi",

        // Weight
        "kg" to "kg",
        "kgs" to "kg",
        "kilogram" to "kg",
        "kilograms" to "kg",
        "pounds" to "lb",
        "pound" to "lb"

    )

    fun parseInput(text: String): Pair<String, String?> {

        val cleaned = text.trim().lowercase()

        val regex = Regex("""^(-?\d*\.?\d+)\s*([a-zA-Z]+)?""")

        val match = regex.find(cleaned) ?: return Pair(text, null)

        val numberPart = match.groupValues[1]

        if (numberPart == "-" || numberPart.isEmpty()) {
            return Pair("", null)
        }

        val unitPart = match.groupValues.getOrNull(2)

        val resolved = unitPart?.let { aliases[it] }

        return Pair(numberPart, resolved)
    }

}