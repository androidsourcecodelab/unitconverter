package com.androidsourcecodelab.unitconverter.model


data class UnitCategory(
    val name: String,
    val iconLabel: String,
    val units: List<UnitItem>,
    val type: ConverterType = ConverterType.LINEAR
)

enum class ConverterType {
    LINEAR,
    NUMBER_BASE,
    TEMPERATURE
}


