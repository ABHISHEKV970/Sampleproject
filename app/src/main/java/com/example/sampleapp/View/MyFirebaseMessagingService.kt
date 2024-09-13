package com.example.sampleapp.View

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "From: ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            //Push Payload to the Moengage SDK

            if (MoEPushHelper.getInstance().isFromMoEngagePlatform(remoteMessage.data)){
                MoEFireBaseHelper.getInstance().passPushPayload(applicationContext, remoteMessage.data)
            }else{
                // your app's business logic to show notification
            }

        }

        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }


    }



    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        Log.i("datatoken", token)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}