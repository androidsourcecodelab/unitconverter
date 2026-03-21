package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object LengthCategory {

    val category = UnitCategory(
        name = "Length",
        iconLabel = "Length",
        units = listOf(
            UnitItem("Meter", "m", 1.0, "length"),

            UnitItem("Kilometer", "km", 1e3, "length"),
            UnitItem("Centimeter", "cm", 1e-2, "length"),
            UnitItem("Millimeter", "mm", 1e-3, "length"),
            UnitItem("Micrometer", "µm", 1e-6, "length"),
            UnitItem("Nanometer", "nm", 1e-9, "length"),

            UnitItem("Mile", "mi", 1609.34, "length"),
            UnitItem("Yard", "yd", 0.9144, "length"),
            UnitItem("Foot", "ft", 0.3048, "length"),
            UnitItem("Inch", "in", 0.0254, "length"),

            UnitItem("Nautical Mile", "nmi", 1852.0, "length"),

            UnitItem("Astronomical Unit", "AU", 1.496e11, "length"),
            UnitItem("Light Year", "ly", 9.4607e15, "length"),
            UnitItem("Parsec", "pc", 3.0857e16, "length")
        )
    )

}