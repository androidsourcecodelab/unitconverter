package com.androidsourcecodelab.unitconverter.engine.format

object StorageFormatStrategy : FormatStrategy {

    override fun format(value: Any): String {
        val v = value as Double

        return when {
            v >= 1e9 -> "${v / 1e9} GB"
            v >= 1e6 -> "${v / 1e6} MB"
            v >= 1e3 -> "${v / 1e3} KB"
            else -> "$v B"
        }
    }
}