package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object PressureCategory {

    val category = UnitCategory(
        name = "Pressure",
        iconLabel =  "Pres",
        units = listOf(
            UnitItem("Pascal", "Pa", 1.0),
            UnitItem("Kilopascal", "kPa", 1000.0),
            UnitItem("Bar", "bar", 100000.0),
            UnitItem("Atmosphere", "atm", 101325.0),
            UnitItem("PSI", "psi", 6894.76)
        )
    )

}