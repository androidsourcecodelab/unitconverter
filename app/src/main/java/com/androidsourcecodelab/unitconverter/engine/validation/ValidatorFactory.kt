package com.androidsourcecodelab.unitconverter.engine.validation

import com.androidsourcecodelab.unitconverter.model.ConverterType
import com.androidsourcecodelab.unitconverter.model.UnitCategory

object ValidatorFactory {

    fun get(category: UnitCategory): InputValidator {

        return when (category.type) {
            ConverterType.NUMBER_BASE -> NumberBaseInputValidator

            ConverterType.DATA_SIZE -> DataSizeInputValidator   // 🔥 add this

            else -> DefaultInputValidator
        }
    }
}