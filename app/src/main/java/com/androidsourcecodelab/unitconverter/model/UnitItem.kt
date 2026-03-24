package com.androidsourcecodelab.unitconverter.model

data class UnitItem(
    val name: String,
    val symbol: String,
    val factor: Double,
    val dimension: String,
    val priority: Int = 0
)
