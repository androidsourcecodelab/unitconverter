package com.androidsourcecodelab.unitconverter.engine.validation

import com.androidsourcecodelab.unitconverter.model.UnitItem

object NumberBaseInputValidator : InputValidator {

    override fun validate(input: String, fromUnit: UnitItem): ValidationResult {

        val (regex, message) = when (fromUnit.symbol) {

            "BIN" -> Regex("^[01]+$") to "Binary supports only 0 and 1"
            "OCT" -> Regex("^[0-7]+$") to "Octal supports only digits 0–7"
            "HEX" -> Regex("^[0-9A-Fa-f]+$") to "Hex supports 0–9 and A–F"
            "DEC" -> Regex("^\\d+$") to "Decimal supports only digits"

            else -> return ValidationResult(false, "Unsupported base")
        }

        return if (input.matches(regex)) {
            ValidationResult(true)
        } else {
            ValidationResult(false, message)
        }
    }
}