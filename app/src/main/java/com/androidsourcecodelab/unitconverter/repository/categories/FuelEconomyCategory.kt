package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object FuelEconomyCategory {

    val category = UnitCategory(
        name = "Fuel Economy",
        iconLabel = "Fuel",
        units = listOf(
            UnitItem("Kilometers per liter", "km/L", 1.0, "fuel"),

            UnitItem("Miles per gallon (US)", "mpg", 0.425144, "fuel"),
            UnitItem("Miles per gallon (UK)", "mpg_imp", 0.354006, "fuel"),

            UnitItem("Kilometers per gallon (US)", "km/gal", 0.264172, "fuel")
        )
    )

}