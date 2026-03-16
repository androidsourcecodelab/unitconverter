package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object PowerCategory {

    val category = UnitCategory(
        name = "Power",
        iconLabel = "Power",
        units = listOf(
            UnitItem("Watt", "W", 1.0),
            UnitItem("Kilowatt", "kW", 1000.0),
            UnitItem("Megawatt", "MW", 1_000_000.0),
            UnitItem("Horsepower", "hp", 745.699872)
        )
    )
}