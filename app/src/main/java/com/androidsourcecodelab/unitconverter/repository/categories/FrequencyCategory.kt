package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object FrequencyCategory {

    val category = UnitCategory(
        name = "Frequency",
        iconLabel = "Freq",
        units = listOf(
            UnitItem("Hertz", "Hz", 1.0, "frequency"),

            UnitItem("Kilohertz", "kHz", 1e3, "frequency"),
            UnitItem("Megahertz", "MHz", 1e6, "frequency"),
            UnitItem("Gigahertz", "GHz", 1e9, "frequency"),

            UnitItem("Revolutions per minute", "rpm", 1.0 / 60.0, "frequency"),
            UnitItem("Radians per second", "rad/s", 1.0 / (2 * Math.PI), "frequency")
        )
    )
}