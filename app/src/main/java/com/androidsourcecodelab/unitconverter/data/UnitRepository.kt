package com.androidsourcecodelab.unitconverter.data

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.repository.categories.AngleCategory
import com.androidsourcecodelab.unitconverter.repository.categories.AreaCategory
import com.androidsourcecodelab.unitconverter.repository.categories.AstronomyCategory
import com.androidsourcecodelab.unitconverter.repository.categories.DataSizeCategory
import com.androidsourcecodelab.unitconverter.repository.categories.DensityCategory
import com.androidsourcecodelab.unitconverter.repository.categories.DurationCategory
import com.androidsourcecodelab.unitconverter.repository.categories.EnergyCategory
import com.androidsourcecodelab.unitconverter.repository.categories.FrequencyCategory
import com.androidsourcecodelab.unitconverter.repository.categories.FuelEconomyCategory
import com.androidsourcecodelab.unitconverter.repository.categories.LengthCategory
import com.androidsourcecodelab.unitconverter.repository.categories.NumberBaseCategory
import com.androidsourcecodelab.unitconverter.repository.categories.PowerCategory
import com.androidsourcecodelab.unitconverter.repository.categories.PressureCategory
import com.androidsourcecodelab.unitconverter.repository.categories.SpeedCategory
import com.androidsourcecodelab.unitconverter.repository.categories.TemperatureCategory
import com.androidsourcecodelab.unitconverter.repository.categories.TimeCategory
import com.androidsourcecodelab.unitconverter.repository.categories.VolumeCategory
import com.androidsourcecodelab.unitconverter.repository.categories.WeightCategory

object UnitRepository {





    val defaultCategories = listOf(
        NumberBaseCategory.category,
        LengthCategory.category,
        WeightCategory.category,
        SpeedCategory.category,
        AstronomyCategory.category,
        AreaCategory.category,
        TemperatureCategory.category,
        VolumeCategory.category,
        DurationCategory.category,
        PressureCategory.category,
        AngleCategory.category,
        EnergyCategory.category,
        FuelEconomyCategory.category,
        DensityCategory.category,
        DataSizeCategory.category
        //FrequencyCategory.category
        //PowerCategory.category
    )

    val allCategories = listOf(
        NumberBaseCategory.category,
        LengthCategory.category,
        WeightCategory.category,
        SpeedCategory.category,
        AstronomyCategory.category,
        AreaCategory.category,
        TemperatureCategory.category,
        VolumeCategory.category,
        DurationCategory.category,
        PressureCategory.category,
        AngleCategory.category,
        EnergyCategory.category,
        FuelEconomyCategory.category,
        DensityCategory.category,
        DataSizeCategory.category,
        FrequencyCategory.category,
        PowerCategory.category,
        TimeCategory.category
    )

    fun getUnitItem(category: UnitCategory, symbol: String): UnitItem? {
        return category.units.find { it.symbol == symbol }
    }

    fun getUnitItemBySymbol(symbol: String): UnitItem? {
        return allCategories
            .asSequence()
            .flatMap { it.units.asSequence() }
            .firstOrNull { it.symbol == symbol }
    }


    val unitMap: Map<String, Pair<UnitCategory, UnitItem>> =
        allCategories.flatMap { category: UnitCategory ->
            category.units.map { unit: UnitItem ->
                unit.symbol to Pair(category, unit)
            }
        }.toMap()

    fun findUnitAcrossCategories(symbol: String)
            : Pair<UnitCategory, UnitItem>? {

        return unitMap[symbol]
    }

    fun findCategoryForUnit(unit: UnitItem): UnitCategory? {

        return allCategories.firstOrNull { category ->
            category.units.any { it.symbol == unit.symbol }
        }
    }

    fun getCategoryByName(name: String): UnitCategory? {
        return allCategories.find { it.name == name }
    }


}
