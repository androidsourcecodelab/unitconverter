package com.androidsourcecodelab.unitconverter.reference

fun findReference(
    value: Double,
    references: List<ReferenceItem>
): ReferenceItem? {

    return references.firstOrNull {
        value >= it.minValue && value <= it.maxValue
    }
}
