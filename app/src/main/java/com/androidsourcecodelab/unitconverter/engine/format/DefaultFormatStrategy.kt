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
            value % 1.0 == 0.0 -> {
                value.toLong().toString()
            }

            absValue >= 1_000_000 || (absValue <= 0.000001 && absValue != 0.0) -> {
                "%.6E".format(value)
            }

            else -> {
                "%.10f".format(value)
                    .trimEnd('0')
                    .trimEnd('.')
            }
        }
    }
}