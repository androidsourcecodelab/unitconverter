package com.androidsourcecodelab.unitconverter.engine.validation

import com.androidsourcecodelab.unitconverter.model.UnitItem

object DefaultInputValidator : InputValidator {

    override fun validate(input: String, fromUnit: UnitItem): ValidationResult {

        val isValid = input.toDoubleOrNull() != null

        return if (isValid) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Enter a valid number")
        }
    }
}