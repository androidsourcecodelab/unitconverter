package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object DataSizeCategory {

    val category = UnitCategory(
        name = "Data Size",
        iconLabel = "Data",
        units = listOf(
            UnitItem("Byte", "B", 1.0, "storage"),

            // Decimal (SI)
            UnitItem("Kilobyte", "KB", 1_000.0, "storage"),
            UnitItem("Megabyte", "MB", 1_000_000.0, "storage"),
            UnitItem("Gigabyte", "GB", 1_000_000_000.0, "storage"),
            UnitItem("Terabyte", "TB", 1_000_000_000_000.0, "storage"),

            // Binary
            UnitItem("Kibibyte", "KiB", 1024.0, "storage"),
            UnitItem("Mebibyte", "MiB", 1024.0 * 1024, "storage"),
            UnitItem("Gibibyte", "GiB", 1024.0 * 1024 * 1024, "storage"),
            UnitItem("Tebibyte", "TiB", 1024.0 * 1024 * 1024 * 1024, "storage"),

            // Bits (optional but powerful)
            UnitItem("Bit", "b", 1.0 / 8, "storage"),
            UnitItem("Kilobit", "Kb", 1000.0 / 8, "storage"),
            UnitItem("Megabit", "Mb", 1_000_000.0 / 8, "storage")
        )
    )

}