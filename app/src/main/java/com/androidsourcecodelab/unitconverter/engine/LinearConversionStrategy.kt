package com.androidsourcecodelab.unitconverter.engine

import com.androidsourcecodelab.unitconverter.model.UnitItem

object LinearConversionStrategy : ConversionStrategy {

    override fun convert(input: String, from: UnitItem, to: UnitItem): String {

        val value = input.toDoubleOrNull()
            ?: throw IllegalArgumentException("Invalid number input")
        val result = value * from.factor / to.factor
        return result.toString()
    }
}