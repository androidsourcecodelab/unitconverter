package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object LengthCategory {

    val category = UnitCategory(
        name = "Length",
        iconLabel = "Length",
        units = listOf(
            UnitItem("Meter", "m", 1.0),
            UnitItem("Kilometer", "km", 1000.0),
            UnitItem("Mile", "mi", 1609.34),
            UnitItem("Foot", "ft", 0.3048)
        )
    )

}