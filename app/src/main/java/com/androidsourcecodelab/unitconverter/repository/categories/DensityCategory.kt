package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object DensityCategory {

    val category = UnitCategory(
        name = "Density",
        iconLabel = "Density",
        units = listOf(
            UnitItem("Kilogram per cubic meter", "kg/m³", 1.0, "density"),
            UnitItem("Gram per cubic centimeter", "g/cm³", 1000.0, "density"),
            UnitItem("Gram per milliliter", "g/mL", 1000.0, "density"),
            UnitItem("Kilogram per liter", "kg/L", 1000.0, "density"),
            UnitItem("Pound per cubic foot", "lb/ft³", 16.0185, "density"),
            UnitItem("Pound per gallon", "lb/gal", 119.826, "density")
        )
    )
}