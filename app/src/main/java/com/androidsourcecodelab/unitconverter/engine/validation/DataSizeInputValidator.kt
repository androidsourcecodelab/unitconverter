package com.androidsourcecodelab.unitconverter.engine.validation


import com.androidsourcecodelab.unitconverter.model.UnitItem

object DataSizeInputValidator : InputValidator {

    override fun validate(input: String, fromUnit: UnitItem): ValidationResult {

        // Only digits (no decimal, no negative, no spaces)
        val isInteger = input.matches(Regex("^\\d+$"))

        return if (isInteger) {
            ValidationResult(true)
        } else {
            ValidationResult(false, "Data Size supports only whole numbers")
        }
    }
}