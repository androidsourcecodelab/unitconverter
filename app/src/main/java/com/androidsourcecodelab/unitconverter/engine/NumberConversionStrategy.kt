package com.androidsourcecodelab.unitconverter.engine

import android.util.Log
import com.androidsourcecodelab.unitconverter.model.UnitItem

object NumberBaseConversionStrategy : ConversionStrategy {

    override fun convert(value: String, from: UnitItem, to: UnitItem): String {

        val input = value
        Log.d("NUMBERBASE", "value in strategy "+input)

        return NumberBaseEngine.convert(
            input,
            from.symbol,
            to.symbol
        )
    }
}