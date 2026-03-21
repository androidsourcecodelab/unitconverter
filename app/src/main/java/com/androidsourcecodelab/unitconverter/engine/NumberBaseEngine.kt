package com.androidsourcecodelab.unitconverter.engine

object NumberBaseEngine {

    fun convert(value: String, from: String, to: String): String {

        return try {

            val base10 = when (from) {
                "BIN" -> value.toLong(2)
                "OCT" -> value.toLong(8)
                "DEC" -> value.toLong(10)
                "HEX" -> value.toLong(16)
                else -> return "Invalid"
            }

            when (to) {
                "BIN" -> base10.toString(2)
                "OCT" -> base10.toString(8)
                "DEC" -> base10.toString(10)
                "HEX" -> base10.toString(16).uppercase()
                else -> "Invalid"
            }

        } catch (e: Exception) {
            "Invalid"   // 🔥 NEVER crash
        }
    }

}