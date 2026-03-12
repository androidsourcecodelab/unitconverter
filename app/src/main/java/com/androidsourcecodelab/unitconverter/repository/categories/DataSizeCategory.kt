package com.androidsourcecodelab.unitconverter.repository.categories

import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object DataSizeCategory {

    val category = UnitCategory(
        name = "Data Size",
        units = listOf(
            UnitItem("Byte", "B", 1.0),
            UnitItem("Kilobyte", "KB", 1024.0),
            UnitItem("Megabyte", "MB", 1024.0 * 1024),
            UnitItem("Gigabyte", "GB", 1024.0 * 1024 * 1024),
            UnitItem("Terabyte", "TB", 1024.0 * 1024 * 1024 * 1024)
        )
    )

}