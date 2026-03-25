package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object TimeCategory {

    val category = UnitCategory(
        name = "Time",
        iconLabel = "Time",
        supportsNearby = false,
        allowNegative = false,
        supportsComposite = true,
        units = listOf(
            UnitItem("Femtosecond", "fs", 1e-15, "time", priority = 1),
            UnitItem("Picosecond", "ps", 1e-12, "time", priority = 2),
            UnitItem("Nanosecond", "ns", 1e-9, "time", priority = 3),
            UnitItem("Microsecond", "µs", 1e-6, "time", priority = 4),
            UnitItem("Millisecond", "ms", 1e-3, "time", priority = 5),

            UnitItem("Second", "s", 1.0, "time", priority = 6),

            UnitItem("Minute", "min", 60.0, "time", priority = 7),
            UnitItem("Hour", "hr", 3600.0, "time", priority = 8),
            UnitItem("Day", "day", 86400.0, "time", priority = 9),
            UnitItem("Week", "week", 604800.0, "time", priority = 10)
        )
    )

}