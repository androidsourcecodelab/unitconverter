package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object EnergyCategory {

    val category = UnitCategory(
        name = "Energy",
        iconLabel = "Energy",
        units = listOf(
            UnitItem("Joule", "J", 1.0, "energy"),

            UnitItem("Kilojoule", "kJ", 1000.0, "energy"),
            UnitItem("Calorie", "cal", 4.184, "energy"),
            UnitItem("Kilocalorie", "kcal", 4184.0, "energy"),

            UnitItem("Watt-hour", "Wh", 3600.0, "energy"),
            UnitItem("Kilowatt-hour", "kWh", 3_600_000.0, "energy"),

            UnitItem("Electronvolt", "eV", 1.602176634e-19, "energy"),
            UnitItem("British Thermal Unit", "BTU", 1055.06, "energy"),

            UnitItem("Foot-pound", "ft·lb", 1.35582, "energy")
        )
    )

}