package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object SpeedCategory {

    val category = UnitCategory(
        name = "Speed",
        iconLabel = "Speed",
        units = listOf(
            UnitItem("Meter per second", "m/s", 1.0, "speed"),

            UnitItem("Kilometer per hour", "km/h", 0.277778, "speed"),
            UnitItem("Miles per hour", "mph", 0.44704, "speed"),

            UnitItem("Foot per second", "ft/s", 0.3048, "speed"),

            UnitItem("Knot", "kt", 0.514444, "speed")
        )
    )

}