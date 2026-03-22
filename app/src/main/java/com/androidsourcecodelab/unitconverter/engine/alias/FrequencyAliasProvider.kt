package com.androidsourcecodelab.unitconverter.engine.alias


class FrequencyAliasProvider : AliasProvider {

    private val aliases = mapOf(

        // Hz
        "hz" to "Hz",
        "hertz" to "Hz",

        // kHz
        "khz" to "kHz",
        "kilohertz" to "kHz",

        // MHz
        "mhz" to "MHz",
        "megahertz" to "MHz",

        // GHz
        "ghz" to "GHz",
        "gigahertz" to "GHz",

        // mHz
        "mhz_small" to "mHz", // handled carefully below
        "millihertz" to "mHz",

        // RPM
        "rpm" to "rpm",
        "revperminute" to "rpm",
        "revolutionperminute" to "rpm",

        // RPS
        "rps" to "rps",
        "revpersecond" to "rps",
        "revolutionpersecond" to "rps",

        // rad/s
        "rad/s" to "rad/s",
        "radianpersecond" to "rad/s"
    )

    override fun resolve(input: String): String? {

        val normalized = input
            .lowercase()
            .replace("-", "")
            .replace(" ", "")

        // ⚠️ Special case: distinguish mHz vs MHz
        if (normalized == "mhz") return "MHz"
        if (normalized == "millihz") return "mHz"

        return aliases[normalized]
            ?: aliases[normalized.removeSuffix("s")]
    }

    override fun resolve(tokens: List<String>, index: Int): Pair<String, Int>? {

        if (index >= tokens.size - 2) return null

        val first = tokens[index]
        val second = tokens[index + 1]
        val third = tokens[index + 2]

        // Handle "revolutions per minute"
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