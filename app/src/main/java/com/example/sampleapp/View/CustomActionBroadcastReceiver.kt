package com.example.sampleapp.View

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CustomActionBroadcastReceiver : BroadcastReceiver() {


    // TASK - 4 Implement CUSTOM ACTION Button Redirection

    override fun onReceive(context: Context, intent: Intent) {
        // Redirect to a specific activity
        val type = intent.getIntExtra("type", 1)

        if (type==1)
        {
            val redirectIntent = Intent(context, MainActivity::class.java)
            redirectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(redirectIntent)
        }
        else
        {
            val redirectIntent = Intent(context, SignUpActivity::class.java)
            redirectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(redirectIntent)
        }

    }
}