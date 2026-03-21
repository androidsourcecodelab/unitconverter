package com.androidsourcecodelab.unitconverter.model

object ConversionHandler {

    /*fun handleInput(input: String): String {

        val parsed = UnitAliasResolver.parseConversion(input)
            ?: return "Invalid input"

        val fromUnit = UnitRegistry.findUnit(parsed.fromUnit)
            ?: return "Unknown unit: ${parsed.fromUnit}"

        val toUnit = UnitRegistry.findUnit(parsed.toUnit)
            ?: return "Unknown unit: ${parsed.toUnit}"

        return try {
            val result = UnitConverterEngine.convert(
                parsed.value,
                fromUnit,
                toUnit
            )

            "$result ${toUnit.symbol}"
        } catch (e: Exception) {
            e.message ?: "Conversion error"
        }
    }*/
}