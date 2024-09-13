package com.example.sampleapp.View

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.example.sampleapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.config.FcmConfig
import com.moengage.core.config.LogConfig
import com.moengage.core.config.NotificationConfig
import com.moengage.core.config.TrackingOptOutConfig
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

//Firebase initialization
        FirebaseApp.initializeApp(this)

        //fetch FCM Token
        fetchFcm_Token()

        //OptOut the activity
        val trackingOptOut = mutableSetOf<Class<*>>()
        trackingOptOut.add(SignUpActivity::class.java)
        val trackingOptOutConfig = TrackingOptOutConfig(
            isCarrierTrackingEnabled = true,
            isDeviceAttributeTrackingEnabled = true,
            trackingOptOut
        )



        val moEngage = MoEngage.Builder(this, "N479J9GSMH8OE6E4IPE5G7NV", DataCenter.DATA_CENTER_1)
            .configureFcm(FcmConfig(true))
            .configureNotificationMetaData(NotificationConfig(R.drawable.ic_notify_small, R.drawable.ic_large_headphone))
            .configureLogs(LogConfig(LogLevel.VERBOSE, true))
            .configureTrackingOptOut(trackingOptOutConfig)
            .build()

        MoEngage.initialiseDefaultInstance(moEngage)



        //Requesting permission on android version 13 and above devices
        MoEPushHelper.getInstance().requestPushPermission(this)


    }

    private fun fetchFcm_Token() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            Log.i("tokenvalue",token)

            MoEFireBaseHelper.getInstance().passPushToken(applicationContext,token)

        })
    }
}