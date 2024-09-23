package com.example.sampleapp.View

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sampleapp.R
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.inbox.ui.adapter.InboxAdapter
import com.moengage.inbox.ui.adapter.InboxListAdapter
import com.moengage.inbox.ui.adapter.ViewHolder


class CustomInboxAdapter() : InboxAdapter() {
    override fun getItemId(position: Int): Long {

        TODO("Not yet implemented")

    }


    override fun getItemViewType(position: Int, inboxMessage: InboxMessage): Int {

        return  1

    }
    override fun onBindViewHolder(
        viewHolder:ViewHolder,
        position: Int,
        inboxMessage: InboxMessage,
        inboxListAdapter: InboxListAdapter
    ) {
        val checkholder = viewHolder as MyViewHolder
        checkholder.bind(inboxMessage)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.item_custom_notification, viewGroup, false)
        return ViewHolder(view)
    }


    inner class MyViewHolder(itemView: View) : ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.notify_title)
        private val content: TextView = itemView.findViewById(R.id.notify_content)
        private val dateValue: TextView = itemView.findViewById(R.id.notifyDate)


        fun bind(inboxMessage: InboxMessage) {
            try {
                title.text = inboxMessage.textContent.title
                content.text = inboxMessage.textContent.message
                dateValue.text = " Date : " + inboxMessage.receivedTime
            }
            catch (e : Exception)
            {
                Log.i("error",e.toString())
            }

        }

    }


}