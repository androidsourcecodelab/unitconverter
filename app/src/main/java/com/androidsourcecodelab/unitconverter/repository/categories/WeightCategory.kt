package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object WeightCategory {

    val category = UnitCategory(
        name = "Weight",
        iconLabel = "Weight",
        units = listOf(
            UnitItem("Kilogram", "kg", 1.0, "mass"),

            UnitItem("Gram", "g", 1e-3, "mass"),
            UnitItem("Milligram", "mg", 1e-6, "mass"),
            UnitItem("Microgram", "µg", 1e-9, "mass"),
            UnitItem("Metric ton", "t", 1000.0, "mass"),

            UnitItem("Pound", "lb", 0.453592, "mass"),
            UnitItem("Ounce", "oz", 0.0283495, "mass"),
            UnitItem("Stone", "st", 6.35029, "mass"),
            UnitItem("US ton", "ton_us", 907.185, "mass"),
            UnitItem("UK ton", "ton_uk", 1016.05, "mass")
        )
    )

}