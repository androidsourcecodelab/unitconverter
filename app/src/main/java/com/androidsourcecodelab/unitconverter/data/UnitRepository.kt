package com.androidsourcecodelab.unitconverter.data

import com.androidsourcecodelab.unitconverter.model.ConverterType
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.repository.categories.AreaCategory
import com.androidsourcecodelab.unitconverter.repository.categories.DataSizeCategory
import com.androidsourcecodelab.unitconverter.repository.categories.LengthCategory
import com.androidsourcecodelab.unitconverter.repository.categories.NumberBaseCategory
import com.androidsourcecodelab.unitconverter.repository.categories.SpeedCategory
import com.androidsourcecodelab.unitconverter.repository.categories.WeightCategory

object UnitRepository {





    val categories = listOf(LengthCategory.category, WeightCategory.category, SpeedCategory.category, DataSizeCategory.category,
        AreaCategory.category, NumberBaseCategory.category)



    val unitMap: Map<String, Pair<UnitCategory, UnitItem>> =
        categories.flatMap { category: UnitCategory ->
            category.units.map { unit: UnitItem ->
                unit.symbol to Pair(category, unit)
            }
        }.toMap()

    fun findUnitAcrossCategories(symbol: String)
            : Pair<UnitCategory, UnitItem>? {

        return unitMap[symbol]
    }

}