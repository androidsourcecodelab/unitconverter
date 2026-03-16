package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.ConverterType
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object TemperatureCategory {

    val category = UnitCategory(
        name = "Temperature",
        iconLabel = "Temp",
        type = ConverterType.TEMPERATURE,
        units = listOf(
            UnitItem("Celsius", "°C", 1.0),
            UnitItem("Fahrenheit", "°F", 1.0),
            UnitItem("Kelvin", "K", 1.0)
        ),
        allowNegative = true
    )
}