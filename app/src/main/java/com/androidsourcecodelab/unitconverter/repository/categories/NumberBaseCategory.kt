package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.ConverterType
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object NumberBaseCategory {

    val category = UnitCategory(
        name = "Number Base",
        type = ConverterType.NUMBER_BASE,
        iconLabel = "Base",
        units = listOf(
            UnitItem("Binary", "BIN", 2.0, "number_base"),
            UnitItem("Octal", "OCT", 8.0, "number_base"),
            UnitItem("Decimal", "DEC", 10.0, "number_base"),
            UnitItem("Hexadecimal", "HEX", 16.0, "number_base")
        ),
        supportsNearby = false
    )

}