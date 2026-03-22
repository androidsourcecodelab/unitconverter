package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object PowerCategory {

    val category = UnitCategory(
        name = "Power",
        iconLabel = "Power",
        units = listOf(
            UnitItem("Watt", "W", 1.0, "power"),

            UnitItem("Kilowatt", "kW", 1e3, "power"),
            UnitItem("Megawatt", "MW", 1e6, "power"),
            UnitItem("Gigawatt", "GW", 1e9, "power"),

            UnitItem("Milliwatt", "mW", 1e-3, "power"),
            UnitItem("Microwatt", "µW", 1e-6, "power"),   // 🔥 added

            UnitItem("Metric horsepower", "hp", 735.49875, "power"),
            UnitItem("Mechanical horsepower", "hp_mech", 745.699872, "power"),

            UnitItem("BTU per hour", "BTU/h", 0.29307107, "power")
        )
    )
}