package com.example.sampleapp.View

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.moengage.core.config.PushKitConfig
import com.moengage.core.config.TrackingOptOutConfig
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.geofence.MoEGeofenceHelper
import com.moengage.hms.pushkit.MoEPushKitHelper
import com.moengage.pushbase.MoEPushHelper
import com.moengage.pushbase.listener.TokenAvailableListener
import com.moengage.pushbase.model.Token


class SampleApplication : Application() {


    override fun onCreate() {

        super.onCreate()
        //Firebase initialization
        FirebaseApp.initializeApp(this)
        //fetch FCM Token
        FetchFcm_Token()

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
            .configureNotificationMetaData(
                NotificationConfig(
                    R.drawable.ic_notify_small,
                    R.drawable.ic_large_headphone
                )
            )
            .configureLogs(LogConfig(LogLevel.VERBOSE, true))
            .configureTrackingOptOut(trackingOptOutConfig)

            //push kit enabling for huwaei
            .configurePushKit(PushKitConfig(true))
            .build()

        MoEngage.initialiseDefaultInstance(moEngage)


        //call back for fetching the token from moengage sdk
        MoEFireBaseHelper.getInstance().addTokenListener(object : TokenAvailableListener {
            override fun onTokenAvailable(token: Token) {

                Log.i("token", token.toString())
                //will receive the token
            }

        })

        //custom push messages
        MoEPushHelper.getInstance().registerMessageListener(CustomPushMessageListener())


        //Requesting permission on android version 13 and above devices
        MoEPushHelper.getInstance().requestPushPermission(this)


        //Geo Fence Permission Enabling
         geo_fence_enable()


        //Call back for fetching the token from HMS Push Kit Server
        MoEPushKitHelper.getInstance().addTokenListener(object : TokenAvailableListener {
            override fun onTokenAvailable(token: Token) {


            }
        })
    }

    private fun geo_fence_enable() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            // Fine Location permission is granted
            // Check if current android version >= 10, if >= 10 check for Background Location permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    println("permission granted")
                    //Configure Geofence
                    MoEGeofenceHelper.getInstance().startGeofenceMonitoring(this)
                    val geofenceHitListener = MyGeofenceHitListener()
                    MoEGeofenceHelper.getInstance().addListener(geofenceHitListener)

                } else {
                    println("pemission not enabled")
                }
            }
        } else {
            // Fine Location Permission is not granted so ask for permission
            println("pemission not enabled")
        }
    }


    private fun FetchFcm_Token() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.i("tokenvalue", token)
            //Token registration using the Normal Push
            MoEFireBaseHelper.getInstance().passPushToken(applicationContext, token)
            //Token registration using PUSH Kit
            MoEPushKitHelper.getInstance().passPushToken(applicationContext, token)
        })
    }

}