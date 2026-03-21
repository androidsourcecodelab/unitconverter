package com.androidsourcecodelab.unitconverter.model

import com.androidsourcecodelab.unitconverter.data.UnitRepository

object UnitRegistry {

    private val unitMap: Map<String, UnitItem> =
        UnitRepository.getAllUnits()
            .associateBy { it.symbol.lowercase() }

    fun findUnit(symbol: String): UnitItem? {
        return unitMap[symbol.lowercase()]
    }
}