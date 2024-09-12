package com.example.sampleapp.View

import android.app.Application
import com.google.firebase.FirebaseApp
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.config.FcmConfig
import com.moengage.core.config.LogConfig

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val moEngage = MoEngage.Builder(this, "N479J9GSMH8OE6E4IPE5G7NV", DataCenter.DATA_CENTER_1)
            .configureFcm(FcmConfig(true))
            .configureLogs(LogConfig(LogLevel.VERBOSE, true))
            .build()

        MoEngage.initialiseDefaultInstance(moEngage)




    }
}