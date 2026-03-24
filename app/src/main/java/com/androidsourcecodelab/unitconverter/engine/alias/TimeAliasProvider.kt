package com.androidsourcecodelab.unitconverter.engine.alias

class TimeAliasProvider : AliasProvider {

    private val aliases = mapOf(

        // Second
        "s" to "s",
        "sec" to "s",
        "second" to "s",
        "seconds" to "s",

        // Millisecond
        "ms" to "ms",
        "millisecond" to "ms",

        // Microsecond
        "us" to "µs",
        "µs" to "µs",
        "microsecond" to "µs",

        // Nanosecond
        "ns" to "ns",
        "nanosecond" to "ns",

        // Picosecond
        "ps" to "ps",
        "picosecond" to "ps",

        // Femtosecond
        "fs" to "fs",
        "femtosecond" to "fs",

        // Minute
        "min" to "min",
        "mins" to "min",
        "minute" to "min",
        "minutes" to "min",

        // Hour
        "h" to "hr",
        "hr" to "hr",
        "hrs" to "hr",
        "hour" to "hr",

        // Day
        "d" to "day",
        "day" to "day",

        // Week
        "w" to "week",
        "wk" to "week",
        "week" to "week"
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

        if (index >= tokens.size - 1) return null

        val first = tokens[index]
        val second = tokens[index + 1]

        val combined = (first + second)
            .lowercase()
            .replace("-", "")
            .replace(" ", "")

        val resolved = aliases[combined]
        if (resolved != null) {
            return resolved to 2
        }

        return null
    }
}