package com.androidsourcecodelab.unitconverter.repository.categories



import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object VolumeCategory {

    val category = UnitCategory(
        name = "Volume",
        iconLabel = "Volume",
        units = listOf(
            UnitItem("Cubic meter", "m³", 1.0, "volume"),

            UnitItem("Cubic kilometer", "km³", 1e9, "volume"),
            UnitItem("Cubic centimeter", "cm³", 1e-6, "volume"),
            UnitItem("Cubic millimeter", "mm³", 1e-9, "volume"),

            UnitItem("Liter", "L", 1e-3, "volume"),
            UnitItem("Milliliter", "mL", 1e-6, "volume"),

            UnitItem("Cubic foot", "ft³", 0.0283168, "volume"),
            UnitItem("Cubic yard", "yd³", 0.764555, "volume"),
            UnitItem("Cubic inch", "in³", 1.6387e-5, "volume"),

            UnitItem("Gallon (US)", "gal_us", 0.00378541, "volume"),
            UnitItem("Gallon (UK)", "gal_imp", 0.00454609, "volume"),

            UnitItem("Quart (US)", "qt_us", 0.000946353, "volume"),
            UnitItem("Pint (US)", "pt_us", 0.000473176, "volume"),
            UnitItem("Cup (US)", "cup_us", 0.000236588, "volume")
        )
    )

}