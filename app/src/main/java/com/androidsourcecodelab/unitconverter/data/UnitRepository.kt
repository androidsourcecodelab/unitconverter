package com.androidsourcecodelab.unitconverter.data

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.repository.categories.AngleCategory
import com.androidsourcecodelab.unitconverter.repository.categories.AreaCategory
import com.androidsourcecodelab.unitconverter.repository.categories.AstronomyCategory
import com.androidsourcecodelab.unitconverter.repository.categories.DensityCategory
import com.androidsourcecodelab.unitconverter.repository.categories.DurationCategory
import com.androidsourcecodelab.unitconverter.repository.categories.EnergyCategory
import com.androidsourcecodelab.unitconverter.repository.categories.FrequencyCategory
import com.androidsourcecodelab.unitconverter.repository.categories.FuelEconomyCategory
import com.androidsourcecodelab.unitconverter.repository.categories.LengthCategory
import com.androidsourcecodelab.unitconverter.repository.categories.NumberBaseCategory
import com.androidsourcecodelab.unitconverter.repository.categories.PressureCategory
import com.androidsourcecodelab.unitconverter.repository.categories.SpeedCategory
import com.androidsourcecodelab.unitconverter.repository.categories.TemperatureCategory
import com.androidsourcecodelab.unitconverter.repository.categories.VolumeCategory
import com.androidsourcecodelab.unitconverter.repository.categories.WeightCategory

object UnitRepository {





    val categories = listOf(NumberBaseCategory.category,LengthCategory.category, WeightCategory.category, SpeedCategory.category,
        AstronomyCategory.category,
        AreaCategory.category, TemperatureCategory.category,
        VolumeCategory.category,
        DurationCategory.category,
        PressureCategory.category,
        AngleCategory.category,
        EnergyCategory.category,
        FuelEconomyCategory.category,
        DensityCategory.category,
        FrequencyCategory.category
        //PowerCategory.category
        )



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

    fun getAllUnits(): List<UnitItem> {
        return categories.flatMap { it.units }
    }

    private val unitToCategoryMap: Map<String, UnitCategory> by lazy {
        val map = mutableMapOf<String, UnitCategory>()

        categories.forEach { category ->
            category.units.forEach { unit ->
                map[unit.symbol.lowercase()] = category
            }
        }

        map
    }

    fun getCategoryForUnit(unit: UnitItem?): UnitCategory? {
        return unit?.symbol?.lowercase()?.let {
            unitToCategoryMap[it]
        }
    }

    fun getCategoryFromSymbol(symbol: String): UnitCategory? {
        return unitMap[symbol.lowercase()]?.first
    }

}