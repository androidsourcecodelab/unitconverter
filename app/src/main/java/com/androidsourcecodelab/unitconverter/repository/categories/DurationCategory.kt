package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object DurationCategory {

    val category = UnitCategory(
        name = "Duration",
        iconLabel = "Dur",
        units = listOf(
            UnitItem("Second", "s", 1.0, "time"),
            UnitItem("Millisecond", "ms", 1e-3, "time"),
            UnitItem("Microsecond", "µs", 1e-6, "time"),
            UnitItem("Nanosecond", "ns", 1e-9, "time"),

            UnitItem("Minute", "min", 60.0, "time"),
            UnitItem("Hour", "h", 3600.0, "time"),
            UnitItem("Day", "d", 86400.0, "time"),
            UnitItem("Week", "wk", 604800.0, "time"),

            UnitItem("Month", "mo", 2628000.0, "time"),   // ~30.44 days avg
            UnitItem("Year", "yr", 31536000.0, "time")    // 365 days
        )
    )

}