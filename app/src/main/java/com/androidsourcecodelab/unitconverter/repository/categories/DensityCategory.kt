package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.ui.iconForCategory

object DensityCategory {

    val category = UnitCategory(
        name = "Density",
        iconLabel = "Density",
        units = listOf(
            UnitItem("Kilogram per cubic meter", "kg/m³", 1.0),
            UnitItem("Gram per cubic centimeter", "g/cm³", 1000.0),
            UnitItem("Pound per cubic foot", "lb/ft³", 16.018463),
            UnitItem("Pound per cubic inch", "lb/in³", 27679.9047)
        )
    )
}