package com.androidsourcecodelab.unitconverter

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import com.androidsourcecodelab.unitconverter.ui.ConverterScreen
import com.androidsourcecodelab.unitconverter.ui.theme.UnitConverterTheme
import com.androidsourcecodelab.unitconverter.viewmodel.ConverterViewModel


class MainActivity : ComponentActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val viewModel = ConverterViewModel()

            setContent {
                UnitConverterTheme {
                    ConverterScreen(viewModel)
                }
            }
        }
}
