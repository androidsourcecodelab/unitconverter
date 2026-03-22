package com.androidsourcecodelab.unitconverter.util

import com.androidsourcecodelab.unitconverter.alias.AreaAliasProvider
import com.androidsourcecodelab.unitconverter.alias.LengthAliasProvider
import com.androidsourcecodelab.unitconverter.alias.VolumeAliasProvider
import com.androidsourcecodelab.unitconverter.alias.WeightAliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.AliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.DataSizeAliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.FrequencyAliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.PowerAliasProvider
import com.androidsourcecodelab.unitconverter.engine.alias.SpeedAliasProvider

object AliasResolver {

    private val providers: List<AliasProvider> = listOf(
        LengthAliasProvider(),
        NumberBaseAliasProvider(),
        WeightAliasProvider(),
        AreaAliasProvider(),
        VolumeAliasProvider(),
        SpeedAliasProvider(),
        DataSizeAliasProvider(),
        FrequencyAliasProvider(),
        PowerAliasProvider()
    )

    fun normalize(tokens: List<String>, index: Int): Pair<String, Int>? {

        val word = tokens[index]

        // 1. Try multi-token resolution first
        for (provider in providers) {
            val multi = provider.resolve(tokens, index)
            if (multi != null) return multi
        }

        // 2. Fallback to single token
        for (provider in providers) {
            val single = provider.resolve(word)
            if (single != null) return single to 1
        }

        return word to 1
    }
}