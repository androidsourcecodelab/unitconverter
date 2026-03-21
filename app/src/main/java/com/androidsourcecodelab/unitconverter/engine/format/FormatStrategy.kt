package com.androidsourcecodelab.unitconverter.engine.format

interface FormatStrategy {
    fun format(value: Any): String
}