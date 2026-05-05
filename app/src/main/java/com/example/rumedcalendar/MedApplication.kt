package com.example.rumedcalendar

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MedApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
