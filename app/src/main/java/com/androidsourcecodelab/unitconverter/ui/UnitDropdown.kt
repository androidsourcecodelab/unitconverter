package com.yourpackage.unitconverter.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.Box
import com.androidsourcecodelab.unitconverter.model.UnitItem

@Composable
fun UnitDropdown(
    units: List<UnitItem>,
    selected: UnitItem,
    onSelected: (UnitItem) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box {

        Button(onClick = { expanded = true }) {
            Text("${selected.symbol} ▼")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            units.forEach { unit ->

                DropdownMenuItem(
                    text = { Text("${unit.symbol} — ${unit.name}")  },
                    onClick = {
                        onSelected(unit)
                        expanded = false
                    }
                )
            }
        }
    }
}
