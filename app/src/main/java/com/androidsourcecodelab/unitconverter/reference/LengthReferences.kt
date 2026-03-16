import com.androidsourcecodelab.unitconverter.reference.ReferenceItem

object LengthReferences {

    val items = listOf(

        ReferenceItem(
            label = "A football field is about 100 meters long.",
            valueInBaseUnit = 100.0,
            minValue = 50.0,
            maxValue = 200.0
        ),

        ReferenceItem(
            label = "The Eiffel Tower is about 330 meters tall.",
            valueInBaseUnit = 330.0,
            minValue = 200.0,
            maxValue = 600.0
        ),

        ReferenceItem(
            label = "Mount Everest is about 8848 meters tall.",
            valueInBaseUnit = 8848.0,
            minValue = 5000.0,
            maxValue = 15000.0
        )
    )
}