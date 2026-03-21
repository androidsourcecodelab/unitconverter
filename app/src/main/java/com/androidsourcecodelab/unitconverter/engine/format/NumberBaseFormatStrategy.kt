package com.androidsourcecodelab.unitconverter.engine.format

object NumberBaseFormatStrategy : FormatStrategy {

    override fun format(value: Any): String {
        return value.toString().uppercase()
    }
}