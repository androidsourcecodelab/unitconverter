package com.androidsourcecodelab.unitconverter.engine.preprocess

import com.androidsourcecodelab.unitconverter.model.UnitCategory

class PreprocessingPipeline(
    private val commands: List<PreprocessingCommand>
) {

    fun execute(
        input: String,
        category: UnitCategory
    ): PreprocessResult {

        for (command in commands) {

            // 🔥 Only run command for matching category
            if (command is CompositeCommand &&
                command.category.name != category.name
            ) continue

            if (command.canHandle(input)) {
                val result = command.process(input)

                if (result !is PreprocessResult.NotApplicable) {
                    return result
                }
            }
        }

        return PreprocessResult.NotApplicable
    }
}