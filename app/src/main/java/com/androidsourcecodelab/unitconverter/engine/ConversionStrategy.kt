package com.androidsourcecodelab.unitconverter.engine

import com.androidsourcecodelab.unitconverter.model.UnitItem

interface ConversionStrategy {
    fun convert(
        value: String,
        from: UnitItem,
        to: UnitItem
    ): String
}