package com.androidsourcecodelab.unitconverter.engine.format

import kotlin.math.abs

object DefaultFormatStrategy : FormatStrategy {

    override fun format(value: Any): String {

        return when (value) {

            is Double -> formatDouble(value)

            is String -> {
                val number = value.toDoubleOrNull()
                if (number != null) {
                    formatDouble(number)   // 🔥 fix here
                } else {
                    value   // NumberBase or non-numeric
                }
            }

            else -> value.toString()
        }
    }

    private fun formatDouble(value: Double): String {

        val absValue = abs(value)

        return when {

            // 🔥 Only convert to Long if within safe range
            value % 1.0 == 0.0 &&
                    absValue <= Long.MAX_VALUE.toDouble() -> {
                value.toLong().toString()
            }

            // 🔥 Large OR very small numbers → scientific
            absValue >= 1_000_000 || (absValue <= 0.000001 && absValue != 0.0) -> {
                //"%.6E".format(value)
                formatScientific(value)
            }

            // 🔥 Normal decimal formatting
            else -> {
                "%.10f".format(value)
                    .trimEnd('0')
                    .trimEnd('.')
            }
        }
    }

    private fun formatScientific(value: Double): String {
        val formatted = "%.6E".format(value)
        val parts = formatted.split("E")

        val mantissa = parts[0].trimEnd('0').trimEnd('.')
        val exponent = parts[1].replace("+", "")

        return "$mantissa × 10^$exponent"
    }
}