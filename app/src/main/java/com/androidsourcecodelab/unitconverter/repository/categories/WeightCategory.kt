package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object WeightCategory {

    val category = UnitCategory(
        name = "Weight",
        iconLabel = "Weight",
        units = listOf(
            UnitItem("Milligram", "mg", 0.000001),
            UnitItem("Gram", "g", 0.001),
            UnitItem("Kilogram", "kg", 1.0),
            UnitItem("Metric ton", "t", 1000.0),
            UnitItem("Pound", "lb", 0.453592),
            UnitItem("Ounce", "oz", 0.0283495)
        )
    )

}