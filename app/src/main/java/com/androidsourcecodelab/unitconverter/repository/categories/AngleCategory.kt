package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object AngleCategory {

    val category = UnitCategory(
        name = "Angle",
        iconLabel="Angle",
        units = listOf(
            UnitItem("Radian", "rad", 1.0, "angle"),
            UnitItem("Degree", "deg", Math.PI / 180.0, "angle"),
            UnitItem("Gradian", "grad", Math.PI / 200.0, "angle"),
            UnitItem("Arcminute", "arcmin", Math.PI / (180.0 * 60), "angle"),
            UnitItem("Arcsecond", "arcsec", Math.PI / (180.0 * 3600), "angle")
        ),
        allowNegative = true
    )

}