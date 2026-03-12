package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.ConverterType
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object NumberBaseCategory {

    val category = UnitCategory(
        name = "Number Base",
        type = ConverterType.NUMBER_BASE,
        units = listOf(
            UnitItem("Decimal", "DEC", 1.0),
            UnitItem("Hexadecimal", "HEX", 1.0),
            UnitItem("Binary", "BIN", 1.0),
            UnitItem("Octal", "OCT", 1.0)
        )
    )

}