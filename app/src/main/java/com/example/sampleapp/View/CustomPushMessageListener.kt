package com.example.sampleapp.View

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.sampleapp.R
import com.moengage.core.internal.utils.getApplicationContext
import com.moengage.pushbase.model.NotificationPayload
import com.moengage.pushbase.push.PushMessageListener
import kotlin.random.Random

class CustomPushMessageListener : PushMessageListener() {


    override fun isNotificationRequired(context: Context, payload: Bundle): Boolean {

        //TASK 2 USING SHARED PREFERENCE

        val preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val storedValue = preferences.getBoolean("notificationstatus", true)

        Log.i("passingvalue", storedValue.toString())

        return preferences.getBoolean("notificationstatus", true)
    }

    override fun customizeNotificationBuilder(
        notificationBuilder: NotificationCompat.Builder,
        context: Context,
        notificationPayload: NotificationPayload
    ) {

        println(notificationPayload)


        var conditioncheck = notificationPayload.payload.getString("push_type")

        conditioncheck?.let {

            if (conditioncheck.equals("moengage")) {


                super.customizeNotificationBuilder(
                    notificationBuilder,
                    context,
                    notificationPayload
                )


            } else {

            }
        }


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

        val conditioncheck = payload.getString("company_name")

        conditioncheck?.let {

            var intent = Intent()

            if (conditioncheck.equals("moengage")) {
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

        // TASK - 4 Implement CUSTOM ACTION Button Redirection

        println(payload)

        val conditionclick = payload == "test"

        if (conditionclick) {

            val intent = Intent(context, CustomActionBroadcastReceiver::class.java)
            intent.putExtra("type", 1)
            context.sendBroadcast(intent)

        } else {

            val intent = Intent(context, CustomActionBroadcastReceiver::class.java)
            intent.putExtra("type", 2)
            context.sendBroadcast(intent)

        }

    }


}