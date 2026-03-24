package com.androidsourcecodelab.unitconverter.engine.preprocess

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

sealed class PreprocessResult {

    data class Success(
        val value: Double,
        val fromUnit: String,
        val toUnit: String? = null ,
        val category: UnitCategory,
        val components: List<Pair<Double, UnitItem>>   // 🔥 ADD THIS
// 🔥 NEW
    ) : PreprocessResult()

    data class Failure(val message: String) : PreprocessResult()
    object NotApplicable : PreprocessResult()
}