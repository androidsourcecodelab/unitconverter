package com.androidsourcecodelab.unitconverter.engine.preprocess

class PreprocessingPipeline(
    private val commands: List<PreprocessingCommand>
) {

    fun execute(input: String): PreprocessResult {

        for (command in commands) {
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