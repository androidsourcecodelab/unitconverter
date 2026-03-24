package com.androidsourcecodelab.unitconverter.engine.preprocess

import com.androidsourcecodelab.unitconverter.data.UnitRepository
import com.androidsourcecodelab.unitconverter.util.AliasResolver

class TimeCompositeCommand(
    private val unitMap: Map<String, Double>
) : PreprocessingCommand {

    private fun pickHighestUnit(
        components: List<Pair<Double, String>>
    ): String {

        return components
            .mapNotNull { (_, symbol) ->
                unitMap[symbol]?.let { symbol }
            }
            .maxByOrNull { symbol ->
                UnitRepository
                    .getUnitItemBySymbol(symbol)
                    ?.priority ?: 0
            }
            ?: "s"
    }

    private fun convertFromSeconds(
        seconds: Double,
        unit: String
    ): Double {

        val unitItem = UnitRepository.getUnitItemBySymbol(unit)
            ?: throw IllegalArgumentException("Unsupported unit: $unit")

        return seconds / unitItem.factor
    }

    override fun canHandle(input: String): Boolean {

        if (input.contains(":")) return false   // ❌ explicitly NOT supported

        val tokens = input.lowercase().split(Regex("\\s+"))

        val toIndex = tokens.indexOf("to")

        // 🔥 MUST contain "to"
        if (toIndex == -1) return false

        // Must have at least 2 value+unit pairs BEFORE "to"
        var count = 0

        for (i in 0 until toIndex - 1) {
            val value = tokens[i].toDoubleOrNull()
            val unit = AliasResolver.normalize(tokens, i + 1)?.first

            if (value != null && unit != null && unitMap.containsKey(unit)) {
                count++
            }
        }

        return count >= 2
    }

    override fun process(input: String): PreprocessResult {

        return try {

            val allTokens = input.lowercase().split(Regex("\\s+"))

            val toIndex = allTokens.indexOf("to")

            // ❌ Case 1: No "to"
            if (toIndex == -1) {
                return PreprocessResult.Failure("Enter the correct format")
            }

            // ❌ Case 2: "to" but no unit
            if (toIndex == allTokens.size - 1) {
                return PreprocessResult.Failure("Enter unit after 'to'")
            }

            // ✅ Extract target unit
            val (toUnit, _) = AliasResolver.normalize(allTokens, toIndex + 1)
                ?: return PreprocessResult.Failure("Enter unit after 'to'")

            // 🔥 Parse only before "to"
            val tokens = allTokens.subList(0, toIndex)

            val components = mutableListOf<Pair<Double, String>>()
            var totalSeconds = 0.0
            var i = 0

            while (i < tokens.size) {

                if (i + 1 >= tokens.size) {
                    return PreprocessResult.Failure("Enter the correct format <from> to <unit>")
                }

                val value = tokens[i].toDoubleOrNull()
                    ?: return PreprocessResult.Failure("Enter the correct format <from> to <unit>")

                val (unit, consumed) = AliasResolver.normalize(tokens, i + 1)
                    ?: return PreprocessResult.Failure("Enter the correct format")

                val factor = unitMap[unit]
                    ?: return PreprocessResult.Failure("Unsupported unit: $unit")

                components.add(value to unit)
                totalSeconds += value * factor

                i += 1 + consumed
            }

            if (components.isEmpty()) {
                return PreprocessResult.Failure("Enter the correct format")
            }

            val timeCategory = UnitRepository.getCategoryByName("Time")
                ?: return PreprocessResult.Failure("Time category not available")

            val highestUnit = pickHighestUnit(components)

            val displayValue = convertFromSeconds(totalSeconds, highestUnit)
            return PreprocessResult.Success(
                value = displayValue,
                fromUnit = highestUnit,
                toUnit = toUnit,
                category = timeCategory,
                components = components
            )

        } catch (e: Exception) {
            PreprocessResult.Failure("Enter the correct format")
        }
    }
}