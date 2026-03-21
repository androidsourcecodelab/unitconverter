package com.androidsourcecodelab.unitconverter.engine.validation

import com.androidsourcecodelab.unitconverter.model.UnitItem

interface InputValidator {
    fun validate(input: String, fromUnit: UnitItem):  ValidationResult
}