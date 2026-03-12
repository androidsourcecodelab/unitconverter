package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object TimeCategory {

    val category = UnitCategory(
        name = "Time",
        units = listOf(
            UnitItem("Millisecond", "ms", 0.001),
            UnitItem("Second", "s", 1.0),
            UnitItem("Minute", "min", 60.0),
            UnitItem("Hour", "h", 3600.0),
            UnitItem("Day", "day", 86400.0),
            UnitItem("Week", "week", 604800.0)
        )
    )

}