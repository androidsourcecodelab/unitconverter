package com.androidsourcecodelab.unitconverter

import com.androidsourcecodelab.unitconverter.engine.preprocess.CompositeCommand
import com.androidsourcecodelab.unitconverter.engine.preprocess.PreprocessResult
import com.androidsourcecodelab.unitconverter.repository.categories.LengthCategory
import com.androidsourcecodelab.unitconverter.repository.categories.TimeCategory
import org.junit.Assert
import org.junit.Test

class CompositePipelineTest {

    // 1️⃣ Time composite → engine returns highest unit (hr), not target (sec)
    @Test
    fun time_composite_basic_engine_behavior() {
        val command = CompositeCommand(TimeCategory.category)

        val result = command.process("1 hour 30 min to sec")

        val success = result as PreprocessResult.Success

        // Engine returns value in highest unit (hours)
        Assert.assertEquals(1.5, success.value, 0.001)
        Assert.assertEquals("hr", success.fromUnit)
        Assert.assertEquals("s", success.toUnit)

        // Composite indicator
        Assert.assertNotNull(success.components)
        Assert.assertEquals(2, success.components!!.size)
    }

    // 2️⃣ Length composite → highest unit (km)
    @Test
    fun length_composite_basic_engine_behavior() {
        val command = CompositeCommand(LengthCategory.category)

        val result = command.process("1 km 20 m to m")

        val success = result as PreprocessResult.Success

        // 1020 m → 1.02 km
        Assert.assertEquals(1.02, success.value, 0.001)
        Assert.assertEquals("km", success.fromUnit)
        Assert.assertEquals("m", success.toUnit)
    }

    // 3️⃣ canHandle should detect valid composite
    @Test
    fun can_handle_valid_composite() {
        val command = CompositeCommand(TimeCategory.category)

        Assert.assertTrue(command.canHandle("1 hour 30 min to sec"))
    }

    // 4️⃣ should NOT treat single unit as composite
    @Test
    fun single_unit_not_composite() {
        val command = CompositeCommand(TimeCategory.category)

        Assert.assertFalse(command.canHandle("1 hour to sec"))
    }

    // 5️⃣ mixed category should fail
    @Test
    fun mixed_category_should_fail() {
        val command = CompositeCommand(LengthCategory.category)

        val result = command.process("1 km 5 sec to m")

        Assert.assertTrue(result is PreprocessResult.Failure)
    }
}