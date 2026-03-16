package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object FrequencyCategory {

    val category = UnitCategory(
        name = "Frequency",
        iconLabel = "Freq",
        units = listOf(
            UnitItem("Hertz", "Hz", 1.0),
            UnitItem("Kilohertz", "kHz", 1000.0),
            UnitItem("Megahertz", "MHz", 1_000_000.0),
            UnitItem("Gigahertz", "GHz", 1_000_000_000.0),
            UnitItem("Revolutions per minute", "RPM", 0.0166667)
        )
    )
}