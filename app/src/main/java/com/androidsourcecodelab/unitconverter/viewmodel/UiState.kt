package com.androidsourcecodelab.unitconverter.viewmodel

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.util.UnitAliasResolver

data class UiState(
    val rawInputText: String = "",     // 🔥 what user typed (UI display)
    val parsedValue: String = "",      // 🔥 clean value for conversion ("12", "FF")

    val result: String = "",

    val fromUnit: UnitItem? = null,
    val toUnit: UnitItem? = null,
    val category: UnitCategory? = null,
    val categories: List<UnitCategory> = emptyList(),

    val parsedCommand: UnitAliasResolver.ParsedCommand? = null,
    val errorMessage: String? = null,
    val suggestions: List<UnitItem> = emptyList()// 🔥 NEW
)
