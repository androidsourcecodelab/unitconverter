package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.ui.iconForCategory

object AngleCategory {

    val category = UnitCategory(
        name = "Angle",
        iconLabel="Angle",
        units = listOf(
            UnitItem("Degree", "deg", 1.0),
            UnitItem("Radian", "rad", 57.2958),
            UnitItem("Grad", "grad", 0.9)
        )
    )

}