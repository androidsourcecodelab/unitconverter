package com.androidsourcecodelab.unitconverter.engine

import com.androidsourcecodelab.unitconverter.model.UnitItem

object TemperatureConversionStrategy : ConversionStrategy {

    override fun convert(value: String, from: UnitItem, to: UnitItem): String {

        val v = value.toDoubleOrNull()
            ?: throw IllegalArgumentException("Invalid numeric input: $value")

        val celsius = when (from.symbol) {
            "°C" -> v
            "°F" -> (v - 32) * 5 / 9
            "K" -> v - 273.15
            else -> v
        }

        val result = when (to.symbol) {
            "°C" -> celsius
            "°F" -> celsius * 9 / 5 + 32
            "K" -> celsius + 273.15
            else -> celsius
        }

        return result.toString()
    }
}