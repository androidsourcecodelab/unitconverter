package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object SpeedCategory {

    val category = UnitCategory(
        name = "Speed",
        iconLabel = "Speed",
        units = listOf(
            UnitItem("Meters per second", "m/s", 1.0),
            UnitItem("Kilometers per hour", "km/h", 0.277778),
            UnitItem("Miles per hour", "mph", 0.44704),
            UnitItem("Knots", "kt", 0.514444),
            UnitItem("Feet per second", "ft/s", 0.3048)
        )
    )

}