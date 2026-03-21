package com.androidsourcecodelab.unitconverter.engine.validation

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
