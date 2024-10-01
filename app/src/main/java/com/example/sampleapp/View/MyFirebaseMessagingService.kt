package com.example.sampleapp.View

import android.R.raw
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sampleapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper
import java.io.DataInputStream
import kotlin.random.Random


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

//        MoEFireBaseHelper.getInstance().passPushPayload(this, remoteMessage.data)
//
//        return ;

        Log.d(TAG, "From: ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            //Push Payload to the Moengage SDK
            if (MoEPushHelper.getInstance().isFromMoEngagePlatform(remoteMessage.data)) {

                println(remoteMessage.data)

                //TASK 2 USING SHARED PREFERENCE
                val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

                val storedValue = sharedPref.getBoolean("notificationstatus", true)

                if (storedValue) {
                    //TASK 1 USING THE BASIC KV PAIR CONDITION CHECKING
                    val conditionMet = remoteMessage.data["push_type"] == "moengage"

                    call_self_handled_notification(remoteMessage)

                    MoEFireBaseHelper.getInstance().passPushPayload(applicationContext, remoteMessage.data)

//checking based on conditions custom notification created

/*                    if (conditionMet) {

                        print("pushamp trigger")
                        //MOENGAGE SDK TRIGGER NOTIFICATION
                        MoEFireBaseHelper.getInstance().passPushPayload(applicationContext, remoteMessage.data)

                    }
                    else {
                        //APP TRIGGER NOTIFICATION
                        if (MoEPushHelper.getInstance()
                                .isFromMoEngagePlatform(remoteMessage.data)
                        ) {

                            call_self_handled_notification(remoteMessage)

//
//                            val Notification_id = Random.nextInt(1000)
//
//                            val Channel_Id = "moengage_channel"
//
//                            MoEPushHelper.getInstance()
//                                .logNotificationReceived(this, remoteMessage.data)
//
//
//                            //TASK 3 BASED ON THE KV PAIR CONDITION REDIRECT THE CLICKS
//
//                            val conditionclick = remoteMessage.data["department_name"] == "solutions"
//
//                            var intent = Intent()
//
//                            if (conditionclick) {
//                                intent = Intent(this, MainActivity::class.java)
//
//                            }
//                            else {
//                                intent = Intent(this, SignUpActivity::class.java)
//                            }
//
//                            val pass_data = remoteMessage.data
//                            val bundle = Bundle()
//
//                            for ((key, value) in pass_data) {
//                                bundle.putString(key, value)
//                            }
//
//                            bundle.putString("pod_name", "no4")
//
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            intent.putExtra("payload", bundle)
//                            val pendingIntent = PendingIntent.getActivity(
//                                this, 0 /* Request code */, intent,
//                                PendingIntent.FLAG_IMMUTABLE
//                            )
//
//                            // Customize the notification
//                            val notificationBuilder = NotificationCompat.Builder(this, Channel_Id)
//                                .setSmallIcon(R.drawable.ic_large_headphone)
//                                .setContentTitle("App Notification")
//                                .setContentText("Just a custom notification")
//                                .setContentIntent(pendingIntent)
//                                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                .setAutoCancel(false)
//                            // Display the notification
//                            try {
//                                val notificationManager =
//                                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    val channel = NotificationChannel(
//                                        Channel_Id, "Some sample title !",
//                                        NotificationManager.IMPORTANCE_DEFAULT
//                                    )
//                                    notificationManager.createNotificationChannel(channel)
//                                }
//                                notificationManager.notify(
//                                    Notification_id,
//                                    notificationBuilder.build()
//                                )
//
//                            } catch (e: Exception) {
//                                Log.i("error", e.toString())
//                            }

                        } else {
                        }

                    }*/



                } else {
                    println("Notification is OFF !")
                }

            } else {
                Log.i("djdjdjdjdjd","hittedhere")
                // your app's business logic to show notification
                call_self_handled_notification(remoteMessage)
            }
        }

        remoteMessage.notification?.let {

            Log.d(TAG, "Message Notification Body: ${it.body}")

        }

    }

    private fun call_self_handled_notification(remoteMessage: RemoteMessage) {

        val Notification_id = Random.nextInt(1000)

        val Channel_Id = "sound"


        var intent = Intent(this, SignUpActivity::class.java)

        val pass_data = remoteMessage.data
        val bundle = Bundle()

        for ((key, value) in pass_data) {
            bundle.putString(key, value)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("payload", bundle)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        //Create sound uri
        val soundfile : Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/" + R.raw.livechat_notify)


        val mBuilder = NotificationCompat.Builder(this, "sound")
            .setSmallIcon(R.drawable.ic_large_headphone)
            .setContentTitle("Test Push From Admin SDK !")
            .setSound(soundfile)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setContentText("Hello Firebase Admin test")

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                Channel_Id,
                "something",
                importance
            )
            notificationChannel.setSound(soundfile, audioAttributes)

            // Set the default notification sound as well
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            mBuilder.setSound(defaultSoundUri)
            mBuilder.setChannelId(Channel_Id)

            mNotificationManager.createNotificationChannel(notificationChannel)
        }

//        mNotificationManager.notify(Notification_id, mBuilder.build())


    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        Log.i("datatoken", token)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}