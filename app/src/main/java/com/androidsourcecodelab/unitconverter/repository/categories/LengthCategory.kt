package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object LengthCategory {

    val category = UnitCategory(
        name = "Length",
        iconLabel = "Length",
        supportsComposite = true,
        allowNegative = false,
        units = listOf(

            // 🔭 Astronomical (largest)
            UnitItem("Parsec", "pc", 3.0857e16, "length", priority = 14),
            UnitItem("Light Year", "ly", 9.4607e15, "length", priority = 13),
            UnitItem("Astronomical Unit", "AU", 1.496e11, "length", priority = 12),

            // 🌊 Large terrestrial
            UnitItem("Nautical Mile", "nmi", 1852.0, "length", priority = 11),
            UnitItem("Mile", "mi", 1609.34, "length", priority = 10),

            // 📏 Metric
            UnitItem("Kilometer", "km", 1e3, "length", priority = 9),
            UnitItem("Meter", "m", 1.0, "length", priority = 8),
            UnitItem("Centimeter", "cm", 1e-2, "length", priority = 7),
            UnitItem("Millimeter", "mm", 1e-3, "length", priority = 6),
            UnitItem("Micrometer", "µm", 1e-6, "length", priority = 5),
            UnitItem("Nanometer", "nm", 1e-9, "length", priority = 4),

            // 📐 Imperial small
            UnitItem("Yard", "yd", 0.9144, "length", priority = 7),
            UnitItem("Foot", "ft", 0.3048, "length", priority = 6),
            UnitItem("Inch", "in", 0.0254, "length", priority = 5)
        )
    )
}