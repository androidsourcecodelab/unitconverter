package com.androidsourcecodelab.unitconverter.engine.preprocess

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem
import com.androidsourcecodelab.unitconverter.util.AliasResolver

class CompositeCommand(
    val category: UnitCategory
) : PreprocessingCommand {

    private fun pickHighestUnit(
        components: List<Pair<Double, UnitItem>>
    ): UnitItem {

        return components
            .maxByOrNull { (_, unit) -> unit.priority }
            ?.second
            ?: components.firstOrNull()?.second
            ?: throw IllegalStateException("No components provided")
    }

    private fun convertFromBaseUnit(
        baseValue: Double,
        unit: UnitItem
    ): Double {
        return baseValue / unit.factor
    }

    override fun canHandle(input: String): Boolean {

        if (input.contains(":")) return false

        val tokens = input.lowercase().split(Regex("\\s+"))
        val toIndex = tokens.indexOf("to")

        if (toIndex == -1) return false

        var i = 0
        var count = 0

        while (i < toIndex) {

            if (i + 1 >= toIndex) break

            val value = tokens[i].toDoubleOrNull()

            if (value == null) {
                i++                // 🔥 skip invalid token
                continue
            }

            val resolved = AliasResolver.normalize(tokens, i + 1)

            if (resolved == null) {
                i++                // 🔥 skip and continue scanning
                continue
            }

            val (unitSymbol, consumed) = resolved

            val unitItem = category.units.find { it.symbol == unitSymbol }

            if (unitItem == null) {
                i++                // 🔥 skip unknown unit
                continue
            }

            count++
            i += 1 + consumed     // move correctly across pair
        }

        return count >= 2
    }

    override fun process(input: String): PreprocessResult {

        return try {

            val allTokens = input.lowercase().split(Regex("\\s+"))
            val toIndex = allTokens.indexOf("to")

            // ❌ No "to"
            if (toIndex == -1) {
                return PreprocessResult.Failure("Enter the correct format")
            }

            // ❌ "to" but no unit
            if (toIndex == allTokens.size - 1) {
                return PreprocessResult.Failure("Enter unit after 'to'")
            }

            // ✅ Resolve target unit
            val (toUnitSymbol, _) = AliasResolver.normalize(allTokens, toIndex + 1,category)
                ?: return PreprocessResult.Failure("Enter unit after 'to'")

            val targetUnit = category.units.find { it.symbol == toUnitSymbol }
                ?: return PreprocessResult.Failure(
                    "Invalid unit for ${category.name}: $toUnitSymbol"
                )

            val tokens = allTokens.subList(0, toIndex)

            val components = mutableListOf<Pair<Double, UnitItem>>()
            var totalInBase = 0.0
            var i = 0

            while (i < tokens.size) {

                if (i + 1 >= tokens.size) {
                    return PreprocessResult.Failure("Enter the correct format <from> to <unit>")
                }
                val value = tokens[i].toDoubleOrNull()
                    ?: return PreprocessResult.Failure("Enter the correct format <from> to <unit>")

                if (!category.allowNegative && value < 0) {
                    return PreprocessResult.Failure(
                        "Negative values are not allowed for ${category.name}"
                    )
                }

                val (unitSymbol, consumed) = AliasResolver.normalize(tokens, i + 1)
                    ?: return PreprocessResult.Failure("Enter the correct format")

                val unitItem = category.units.find { it.symbol == unitSymbol }
                    ?: return PreprocessResult.Failure(
                        "Invalid unit for ${category.name}: $unitSymbol"
                    )

                components.add(value to unitItem)
                totalInBase += value * unitItem.factor

                i += 1 + consumed
            }

            if (components.isEmpty()) {
                return PreprocessResult.Failure("Enter the correct format")
            }

            val highestUnit = pickHighestUnit(components)

            val displayValue = convertFromBaseUnit(totalInBase, highestUnit)

            return PreprocessResult.Success(
                value = displayValue,
                fromUnit = highestUnit.symbol,
                toUnit = targetUnit.symbol,
                category = category,
                components = components
            )

        } catch (e: Exception) {
            PreprocessResult.Failure("Enter the correct format")
        }
    }
}