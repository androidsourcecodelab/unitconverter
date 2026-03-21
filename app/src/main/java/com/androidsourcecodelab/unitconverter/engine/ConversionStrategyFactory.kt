package com.androidsourcecodelab.unitconverter.engine

import com.androidsourcecodelab.unitconverter.model.ConverterType
import com.androidsourcecodelab.unitconverter.model.UnitCategory

object ConversionStrategyFactory {

    fun getStrategy(category: UnitCategory): ConversionStrategy {

        return when (category.type) {

            ConverterType.NUMBER_BASE -> NumberBaseConversionStrategy

            ConverterType.TEMPERATURE -> TemperatureConversionStrategy

            else -> LinearConversionStrategy
        }
    }
}