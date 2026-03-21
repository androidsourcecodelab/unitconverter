package com.androidsourcecodelab.unitconverter.engine

import com.androidsourcecodelab.unitconverter.model.UnitItem

object UnitConverterEngine {

    fun convert(
        value: Double,
        from: UnitItem,
        to: UnitItem
    ): Double {

        require(from.dimension == to.dimension) {
            "Cannot convert ${from.dimension} to ${to.dimension}"
        }

        return value * from.factor / to.factor
    }
}