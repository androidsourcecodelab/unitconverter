package com.yourpackage.unitconverter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.androidsourcecodelab.unitconverter.model.UnitItem

@Composable


fun UnitDropdown(
    units: List<UnitItem>,
    selected: UnitItem?,   // 🔥 changed to nullable
    onSelected: (UnitItem) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box {

        Button(onClick = { expanded = true }) {

            val label = selected?.symbol ?: "Select"

            Text("$label ▼")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            units.forEach { unit ->

                DropdownMenuItem(
                    text = { Text("${unit.symbol} — ${unit.name}") },
                    onClick = {
                        onSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}