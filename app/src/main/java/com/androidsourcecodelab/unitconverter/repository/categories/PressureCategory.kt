package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object PressureCategory {

    val category = UnitCategory(
        name = "Pressure",
        iconLabel =  "Pres",
        units = listOf(
            UnitItem("Pascal", "Pa", 1.0, "pressure"),

            UnitItem("Kilopascal", "kPa", 1e3, "pressure"),
            UnitItem("Megapascal", "MPa", 1e6, "pressure"),

            UnitItem("Bar", "bar", 1e5, "pressure"),
            UnitItem("Millibar", "mbar", 100.0, "pressure"),

            UnitItem("Atmosphere", "atm", 101325.0, "pressure"),

            UnitItem("Pounds per square inch", "psi", 6894.76, "pressure"),

            UnitItem("Torr", "Torr", 133.322, "pressure"),
            UnitItem("Millimeter of mercury", "mmHg", 133.322, "pressure")
        )
    )

}