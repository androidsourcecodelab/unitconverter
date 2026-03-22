package com.androidsourcecodelab.unitconverter.engine.format

import com.androidsourcecodelab.unitconverter.model.ConverterType
import com.androidsourcecodelab.unitconverter.model.UnitCategory

object FormatStrategyFactory {

    fun get(category: UnitCategory): FormatStrategy {

        return when (category.type) {

            ConverterType.NUMBER_BASE -> NumberBaseFormatStrategy

            else -> DefaultFormatStrategy
        }
    }
}