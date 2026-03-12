package com.androidsourcecodelab.unitconverter.data

object BaseConverter {

    fun parse(input: String, base: String): Long? {

        return try {

            when (base.lowercase()) {

                "dec" -> input.toLong()

                "hex" -> input.toLong(16)

                "bin" -> input.toLong(2)

                "oct" -> input.toLong(8)

                else -> null
            }

        } catch (e: Exception) {
            null
        }
    }

    fun format(value: Long, base: String): String {

        return when (base.lowercase()) {

            "dec" -> value.toString()

            "hex" -> value.toString(16).uppercase()

            "bin" -> value.toString(2)

            "oct" -> value.toString(8)

            else -> ""
        }
    }

    fun convert(input: String, from: String, to: String): String {

        val value = parse(input, from) ?: return ""

        return format(value, to)
    }
}