package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object AreaCategory {

    val category = UnitCategory(
        name = "Area",
        units = listOf(
            UnitItem("Square meter", "m²", 1.0),
            UnitItem("Square kilometer", "km²", 1_000_000.0),
            UnitItem("Square foot", "ft²", 0.092903),
            UnitItem("Square yard", "yd²", 0.836127),
            UnitItem("Acre", "acre", 4046.86),
            UnitItem("Hectare", "ha", 10_000.0)
        )
    )

}