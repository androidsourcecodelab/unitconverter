package com.androidsourcecodelab.unitconverter.engine.format

import java.text.DecimalFormat

object DefaultFormatStrategy : FormatStrategy {

    private val formatter = DecimalFormat("#.######")
    private val scientificFormatter = DecimalFormat("0.#####E0")

    override fun format(value: Any): String {

        return when (value) {

            is Double -> {
                val abs = kotlin.math.abs(value)

                if (abs >= 1_000_000 || (abs <= 0.001 && abs != 0.0)) {
                    scientificFormatter.format(value)
                } else {
                    formatter.format(value)
                }
            }

            is String -> value

            else -> value.toString()
        }
    }
}