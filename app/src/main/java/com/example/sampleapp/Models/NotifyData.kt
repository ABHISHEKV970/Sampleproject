package com.example.sampleapp.Models

import android.app.Notification
import com.moengage.inbox.core.model.MediaContent
import com.moengage.inbox.core.model.TextContent
import org.json.JSONObject

data class NotifyData(
    val id: Long,
    val campaignId: String,
    val textContent: TextContent,
    val action: List<Notification.Action>,
    var isClicked: Boolean,
    val tag: String,
    val receivedTime: String,
    val expiry: String,
    val mediaContent: MediaContent? = null,
    val payload: JSONObject
)