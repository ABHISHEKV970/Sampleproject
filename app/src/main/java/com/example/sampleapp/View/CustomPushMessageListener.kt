package com.example.sampleapp.View

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sampleapp.R
import com.google.firebase.messaging.FirebaseMessagingService.NOTIFICATION_SERVICE
import com.moengage.core.internal.utils.getApplicationContext
import com.moengage.pushbase.model.NotificationPayload
import com.moengage.pushbase.push.PushMessageListener
import kotlin.random.Random

class CustomPushMessageListener : PushMessageListener() {



    override fun isNotificationRequired(context: Context, payload: Bundle): Boolean {

        //TASK 2 USING SHARED PREFERENCE

        val preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val storedValue = preferences.getBoolean("notificationstatus",true)

        Log.i("passingvalue",storedValue.toString())

        return preferences.getBoolean("notificationstatus", true)
    }

    override fun customizeNotificationBuilder(
        notificationBuilder: NotificationCompat.Builder,
        context: Context,
        notificationPayload: NotificationPayload)
    {
        super.customizeNotificationBuilder(notificationBuilder, context, notificationPayload)

        // customise the notification builder
        notificationBuilder.setSmallIcon(R.drawable.ic_large_headphone)
        notificationBuilder.setContentTitle("Title has been changed !")
        notificationBuilder.setContentText("Changed using the custom notification builder.")
//        notificationBuilder.setContentIntent(pendingIntent) // Sets the PendingIntent to launch the activity when notification is clicked
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))


    }

    override fun customizeNotification(
        notification: Notification,
        context: Context,
        payload: Bundle
    ) {
        super.customizeNotification(notification, context, payload)
        //customize the notification here
    }

    override fun onNotificationReceived(context: Context, payload: Bundle) {

        super.onNotificationReceived(context, payload)

        //call business logics on when the push notification is received
    }


    override fun onNotificationClick(activity: Activity, payload: Bundle): Boolean {

        //TASK 3 BASED ON THE KV PAIR CONDITION REDIRECT THE CLICKS

        val conditioncheck = payload.getString("gcm_title")

        conditioncheck?.let {

            var intent = Intent()

            if (conditioncheck.equals("test")) {
                intent = Intent(activity, MainActivity::class.java)

            } else {
                intent = Intent(activity, SignUpActivity::class.java)
            }


            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("payload", payload)
            val pendingIntent = PendingIntent.getActivity(
                activity, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            pendingIntent.send()



        }



        return true
    }

    override fun onNotificationCleared(context: Context, payload: Bundle) {
        super.onNotificationCleared(context, payload)
        // callback for notification cleared.
    }

    override fun handleCustomAction(context: Context, payload: String) {
        super.handleCustomAction(context, payload)

//        println(payload)
//
//        val conditionclick = payload == "test"
//
//
//        if(conditionclick)
//        {
//
//
//            val intent = Intent(context, CustomActionBroadcastReceiver::class.java)
//            intent.putExtra("type",1)
//            context.sendBroadcast(intent)
//
//        }
//        else
//        {
//
//            val intent = Intent(context, CustomActionBroadcastReceiver::class.java)
//            intent.putExtra("type",2)
//            context.sendBroadcast(intent)
//
//        }






    }


}