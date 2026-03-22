package com.androidsourcecodelab.unitconverter.engine.alias



class SpeedAliasProvider : AliasProvider {

    private val aliases = mapOf(

        // m/s
        "m/s" to "m/s",
        "mps" to "m/s",
        "meterpersecond" to "m/s",
        "metrepersecond" to "m/s",

        // km/h
        "km/h" to "km/h",
        "kmh" to "km/h",
        "kilometerperhour" to "km/h",
        "kilometreperhour" to "km/h",

        // mph
        "mph" to "mph",
        "mileperhour" to "mph",

        // ft/s
        "ft/s" to "ft/s",
        "fps" to "ft/s",
        "footpersecond" to "ft/s",

        // knots
        "kt" to "kt",
        "knot" to "kt",
        "knots" to "kt",

        // cm/s
        "cm/s" to "cm/s",
        "cmps" to "cm/s",
        "centimeterpersecond" to "cm/s",

        // mm/s
        "mm/s" to "mm/s",
        "mmps" to "mm/s",
        "millimeterpersecond" to "mm/s",

        // km/s
        "km/s" to "km/s",
        "kmps" to "km/s",
        "kilometerpersecond" to "km/s",

        // mi/s
        "mi/s" to "mi/s",
        "mips" to "mi/s",
        "milepersecond" to "mi/s"
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

        // Handle "km per hour"
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