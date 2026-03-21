package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object AstronomyCategory {

    val category = UnitCategory(
        name = "Astronomy",
        iconLabel = "Astro",
        units = listOf(
            UnitItem("Astronomical Unit", "AU", 1.496e11, "length"),
            UnitItem("Light Year", "ly", 9.4607e15, "length"),
            UnitItem("Parsec", "pc", 3.0857e16, "length"),
            UnitItem("Kiloparsec", "kpc", 3.0857e19, "length"),
            UnitItem("Megaparsec", "Mpc", 3.0857e22, "length")
        )
    )
}