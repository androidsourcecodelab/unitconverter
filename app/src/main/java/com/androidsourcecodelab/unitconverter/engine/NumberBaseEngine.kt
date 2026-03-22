package com.androidsourcecodelab.unitconverter.engine

import java.math.BigInteger

object NumberBaseEngine {

    fun convert(value: String, from: String, to: String): String {

        return try {

            val normalized = value.trim()

            val base10 = when (from) {
                "BIN" -> BigInteger(normalized, 2)
                "OCT" -> BigInteger(normalized, 8)
                "DEC" -> BigInteger(normalized, 10)
                "HEX" -> BigInteger(normalized, 16)
                else -> return "Invalid"
            }

            when (to) {
                "BIN" -> base10.toString(2)
                "OCT" -> base10.toString(8)
                "DEC" -> base10.toString(10)
                "HEX" -> base10.toString(16).uppercase()
                else -> "Invalid"
            }

        } catch (e: Exception) {
            "Invalid"
        }
    }
}