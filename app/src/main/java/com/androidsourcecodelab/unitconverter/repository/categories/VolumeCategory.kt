package com.androidsourcecodelab.unitconverter.repository.categories



import com.androidsourcecodelab.unitconverter.model.UnitCategory
import com.androidsourcecodelab.unitconverter.model.UnitItem

object VolumeCategory {

    val category = UnitCategory(
        name = "Volume",
        units = listOf(
            UnitItem("Milliliter", "ml", 0.001),
            UnitItem("Liter", "L", 1.0),
            UnitItem("Cubic meter", "m³", 1000.0),
            UnitItem("Teaspoon", "tsp", 0.00492892),
            UnitItem("Tablespoon", "tbsp", 0.0147868),
            UnitItem("Cup", "cup", 0.236588),
            UnitItem("Gallon", "gal", 3.78541)
        )
    )

}