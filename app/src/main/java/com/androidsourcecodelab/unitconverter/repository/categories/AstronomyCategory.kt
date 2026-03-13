package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object AstronomyCategory {

    val category = UnitCategory(
        name = "Astronomy",
        iconLabel = "Astro",
        units = listOf(
            UnitItem("Astronomical Unit", "AU", 1.0),
            UnitItem("Light Second", "ls", 0.00200399),
            UnitItem("Light Minute", "lm", 0.120239),
            UnitItem("Light Year", "ly", 63241.077),
            UnitItem("Parsec", "pc", 206264.806)
        )
    )
}