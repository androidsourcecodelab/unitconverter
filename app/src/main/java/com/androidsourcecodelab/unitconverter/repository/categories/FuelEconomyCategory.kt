package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object FuelEconomyCategory {

    val category = UnitCategory(
        name = "Fuel Economy",
        units = listOf(
            UnitItem("Kilometers per liter", "km/L", 1.0),
            UnitItem("Miles per gallon (US)", "mpg", 0.425144),
            UnitItem("Miles per gallon (UK)", "mpgUK", 0.354006)
        )
    )

}