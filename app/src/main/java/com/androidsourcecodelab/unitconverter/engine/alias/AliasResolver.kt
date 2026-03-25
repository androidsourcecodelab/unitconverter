package com.androidsourcecodelab.unitconverter.util

import android.health.connect.datatypes.units.Volume
import com.androidsourcecodelab.unitconverter.alias.AreaAliasProvider
import com.androidsourcecodelab.unitconverter.alias.LengthAliasProvider
import com.androidsourcecodelab.unitconverter.alias.VolumeAliasProvider
import com.androidsourcecodelab.unitconverter.alias.WeightAliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.AliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.DataSizeAliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.FrequencyAliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.PowerAliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.SpeedAliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.TimeAliasProvider
import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.repository.categories.AreaCategory
import com.androidsourcecodelab.unitconverter.repository.categories.DataSizeCategory
import com.androidsourcecodelab.unitconverter.repository.categories.FrequencyCategory
import com.androidsourcecodelab.unitconverter.repository.categories.LengthCategory
import com.androidsourcecodelab.unitconverter.repository.categories.NumberBaseCategory
import com.androidsourcecodelab.unitconverter.repository.categories.PowerCategory
import com.androidsourcecodelab.unitconverter.repository.categories.SpeedCategory
import com.androidsourcecodelab.unitconverter.repository.categories.TimeCategory
import com.androidsourcecodelab.unitconverter.repository.categories.VolumeCategory
import com.androidsourcecodelab.unitconverter.repository.categories.WeightCategory

object AliasResolver {

    // 🔹 Global providers (for detection phase)
    private val providers: List<AliasProvider> = listOf(
        LengthAliasProvider(),
        NumberBaseAliasProvider(),
        WeightAliasProvider(),
        AreaAliasProvider(),
        VolumeAliasProvider(),
        SpeedAliasProvider(),
        DataSizeAliasProvider(),
        FrequencyAliasProvider(),
        PowerAliasProvider(),
        TimeAliasProvider()
    )

    // 🔹 Category-specific providers (for strict parsing)
    private val providersMap: Map<String, AliasProvider> = mapOf(
        LengthCategory.category.name to LengthAliasProvider(),
        NumberBaseCategory.category.name to NumberBaseAliasProvider(),
        WeightCategory.category.name to VolumeAliasProvider(),
        AreaCategory.category.name to AreaAliasProvider(),
        VolumeCategory.category.name to VolumeAliasProvider(),
        SpeedCategory.category.name to SpeedAliasProvider(),
        DataSizeCategory.category.name to DataSizeAliasProvider(),
        FrequencyCategory.category.name to FrequencyAliasProvider(),
        PowerCategory.category.name to PowerAliasProvider(),
        TimeCategory.category.name to TimeAliasProvider()
    )

    /**
     * 🔹 GLOBAL NORMALIZE (used for category detection)
     */
    fun normalize(tokens: List<String>, index: Int): Pair<String, Int>? {

        val word = tokens[index]

        // 1. Multi-token
        for (provider in providers) {
            val multi = provider.resolve(tokens, index)
            if (multi != null) return multi
        }

        // 2. Single-token
        for (provider in providers) {
            val single = provider.resolve(word)
            if (single != null) return single to 1
        }

        return null
    }

    /**
     * 🔹 CATEGORY-SCOPED NORMALIZE (used in CompositeCommand)
     */
    fun normalize(
        tokens: List<String>,
        index: Int,
        category: UnitCategory
    ): Pair<String, Int>? {

        val provider = providersMap[category.name] ?: return null

        val word = tokens[index]

        // 1. Multi-token
        provider.resolve(tokens, index)?.let { return it }

        // 2. Single-token
        provider.resolve(word)?.let { return it to 1 }

        return null
    }
}