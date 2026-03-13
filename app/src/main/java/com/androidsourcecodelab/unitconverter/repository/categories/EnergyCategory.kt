package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object EnergyCategory {

    val category = UnitCategory(
        name = "Energy",
        iconLabel = "Energy",
        units = listOf(
            UnitItem("Joule", "J", 1.0),
            UnitItem("Kilojoule", "kJ", 1000.0),
            UnitItem("Calorie", "cal", 4.184),
            UnitItem("Kilocalorie", "kcal", 4184.0),
            UnitItem("Watt hour", "Wh", 3600.0),
            UnitItem("Kilowatt hour", "kWh", 3_600_000.0)
        )
    )

}