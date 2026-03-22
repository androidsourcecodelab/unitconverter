package com.androidsourcecodelab.unitconverter.engine.alias

class DataSizeAliasProvider : AliasProvider {

    private val aliases = mapOf(

        // Byte
        "b" to "B",
        "byte" to "B",

        // -------------------------
        // SI (Decimal)
        // -------------------------
        "kb" to "KB",
        "kilobyte" to "KB",

        "mb" to "MB",           // 🔥 practical mapping
        "megabyte" to "MB",

        "gb" to "GB",
        "gigabyte" to "GB",

        "tb" to "TB",
        "terabyte" to "TB",

        // -------------------------
        // Binary
        // -------------------------
        "kib" to "KiB",
        "kibibyte" to "KiB",

        "mib" to "MiB",
        "mebibyte" to "MiB",

        "gib" to "GiB",
        "gibibyte" to "GiB",

        "tib" to "TiB",
        "tebibyte" to "TiB",

        // -------------------------
        // Bits (less common)
        // -------------------------
        "bit" to "b",

        "kbit" to "Kb",
        "kilobit" to "Kb",

        "mbit" to "Mb",
        "megabit" to "Mb"
    )

    override fun resolve(input: String): String? {
        val normalized = input
            .lowercase()
            .replace("-", "")
            .replace(" ", "")

        return aliases[normalized]
            ?: aliases[normalized.removeSuffix("s")]
    }
}