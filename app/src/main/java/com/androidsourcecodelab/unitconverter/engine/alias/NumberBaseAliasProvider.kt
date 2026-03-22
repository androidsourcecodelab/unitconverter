package com.androidsourcecodelab.unitconverter.util

import com.androidsourcecodelab.unitconverter.engine.alias.AliasProvider

class NumberBaseAliasProvider : AliasProvider {

    private val aliases = mapOf(
        "bin" to "BIN",
        "binary" to "BIN",

        "oct" to "OCT",
        "octal" to "OCT",

        "dec" to "DEC",
        "decimal" to "DEC",

        "hex" to "HEX",
        "hexadecimal" to "HEX"
    )

    override fun resolve(input: String): String? {
        val lower = input.lowercase()

        return aliases[lower]
            ?: aliases[lower.removeSuffix("s")]
    }
}