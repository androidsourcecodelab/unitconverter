package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.ui.iconForCategory

object AngleCategory {

    val category = UnitCategory(
        name = "Angle",
        iconLabel="Angle",
        units = listOf(
            UnitItem("Degree", "degree", 1.0),
            UnitItem("Radian", "radian", 57.2958),
            UnitItem("Grad", "gradient", 0.9)
        ),
        allowNegative = true
    )

}