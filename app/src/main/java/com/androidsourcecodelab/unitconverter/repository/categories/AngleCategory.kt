package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object AngleCategory {

    val category = UnitCategory(
        name = "Angle",
        units = listOf(
            UnitItem("Degree", "deg", 1.0),
            UnitItem("Radian", "rad", 57.2958),
            UnitItem("Grad", "grad", 0.9)
        )
    )

}