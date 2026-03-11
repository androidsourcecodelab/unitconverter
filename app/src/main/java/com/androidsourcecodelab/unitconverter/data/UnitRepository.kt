package com.androidsourcecodelab.unitconverter.data

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object UnitRepository {

    val length = UnitCategory(
        name = "Length",
        units = listOf(
            UnitItem("Meter", "m", 1.0),
            UnitItem("Kilometer", "km", 1000.0),
            UnitItem("Mile", "mi", 1609.34),
            UnitItem("Foot", "ft", 0.3048),
            UnitItem("Inch", "in", 0.0254)
        )



    )

    val weight = UnitCategory(
        "Weight",
        listOf(
            UnitItem("Kilogram", "kg", 1.0),
            UnitItem("Gram", "g", 0.001),
            UnitItem("Pound", "lb", 0.453592),
            UnitItem("Ounce", "oz", 0.0283495)
        )
    )

    val speed = UnitCategory(
        "Speed",
        listOf(
            UnitItem("Meters/sec", "m/s", 1.0),
            UnitItem("Km/hour", "km/h", 0.277778),
            UnitItem("Miles/hour", "mph", 0.44704)
        )
    )

    fun findUnitAcrossCategories(symbol: String): Pair<UnitCategory, UnitItem>? {

        categories.forEach { category ->

            val unit = category.units.find { it.symbol == symbol }

            if (unit != null) {
                return Pair(category, unit)
            }
        }

        return null
    }

    val categories = listOf(length,weight,speed)

}