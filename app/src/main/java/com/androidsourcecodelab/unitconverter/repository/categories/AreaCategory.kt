package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object AreaCategory {

    val category = UnitCategory(
        name = "Area",
        "Area",
        units = listOf(
            UnitItem("Square meter", "m²", 1.0, "area"),
            UnitItem("Square kilometer", "km²", 1_000_000.0, "area"),
            UnitItem("Square centimeter", "cm²", 1e-4, "area"),
            UnitItem("Square millimeter", "mm²", 1e-6, "area"),
            UnitItem("Square foot", "ft²", 0.092903, "area"),
            UnitItem("Square yard", "yd²", 0.836127, "area"),
            UnitItem("Square inch", "in²", 0.00064516, "area"),
            UnitItem("Acre", "acre", 4046.86, "area"),
            UnitItem("Hectare", "ha", 10_000.0, "area")
        )
    )

}