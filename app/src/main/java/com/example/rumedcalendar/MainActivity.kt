package com.example.rumedcalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.rumedcalendar.ui.MedCalApp
import com.example.rumedcalendar.ui.theme.MedCalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedCalTheme {
                MedCalApp()
            }
        }
    }
}
