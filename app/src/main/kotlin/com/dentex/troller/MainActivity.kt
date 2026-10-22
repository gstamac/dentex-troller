package com.dentex.troller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dentex.troller.ui.DentexApp
import com.dentex.troller.ui.theme.DentexTrollerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DentexTrollerTheme {
                DentexApp()
            }
        }
    }
}
