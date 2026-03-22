package com.androidsourcecodelab.unitconverter.alias

import com.androidsourcecodelab.unitconverter.engine.alias.AliasProvider

class LengthAliasProvider : AliasProvider {

    private val aliases = mapOf(
        "km" to "km",
        "kilometer" to "km",
        "kilometre" to "km",

        "m" to "m",
        "meter" to "m",

        "nm" to "nm",
        "nanometer" to "nm",

        "mi" to "mi",
        "mile" to "mi"
    )

    override fun resolve(input: String): String? {
        val lower = input.lowercase()

        return aliases[lower]
            ?: aliases[lower.removeSuffix("s")]
    }
}