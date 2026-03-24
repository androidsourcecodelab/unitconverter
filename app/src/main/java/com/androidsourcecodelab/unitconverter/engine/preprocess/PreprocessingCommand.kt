package com.androidsourcecodelab.unitconverter.engine.preprocess

interface PreprocessingCommand {
    fun canHandle(input: String): Boolean
    fun process(input: String): PreprocessResult
}