package com.androidsourcecodelab.unitconverter

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.androidsourcecodelab.unitconverter.manager.CategoryManager

import com.androidsourcecodelab.unitconverter.ui.ConverterScreen
import com.androidsourcecodelab.unitconverter.ui.theme.UnitConverterTheme
import com.androidsourcecodelab.unitconverter.viewmodel.ConverterViewModel


class MainActivity : ComponentActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // 🔥 MUST be before ViewModel usage
            CategoryManager.init(applicationContext)

            val viewModel = ConverterViewModel(application)

            setContent {
                UnitConverterTheme {
                    ConverterScreen(viewModel)
                }
            }
        }
}
