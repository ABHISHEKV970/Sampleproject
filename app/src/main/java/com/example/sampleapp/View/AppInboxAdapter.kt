package com.example.sampleapp.View

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapp.R
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.core.listener.OnMessagesAvailableListener
import com.moengage.inbox.core.model.InboxData
import com.moengage.inbox.core.model.InboxMessage


class AppInboxAdapter(private val onDeleteClickListener: OnDeleteClickListener) : RecyclerView.Adapter<AppInboxAdapter.ViewHolder>(),
    OnMessagesAvailableListener {

    var inboxData: InboxData? = null


    override fun onMessagesAvailable(inboxData: InboxData?) {
        this.inboxData = inboxData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_custom_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = inboxData?.inboxMessages?.get(position)
        holder.bind(message,position)

    }

    override fun getItemCount(): Int {
//        return inboxData?.inboxMessages?.size ?: 0
        val count = inboxData?.inboxMessages?.size ?: 0
        Log.d("Item count", "Size: $count")
        return count
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.notify_title)
        private val content: TextView = itemView.findViewById(R.id.notify_content)
        private val dateValue: TextView = itemView.findViewById(R.id.notifyDate)
        private val delete: ImageButton = itemView.findViewById(R.id.delete)

        fun bind(message: InboxMessage?, position: Int) {
            try {
                if (message != null) {
                    title.text = message.textContent.title
                    content.text = message.textContent.message
                    dateValue.text = " Date : " + message.receivedTime

                    itemView.setOnClickListener {
                        MoEInboxHelper.getInstance().trackMessageClicked(itemView.context, message)

                        itemView.setBackgroundColor(Color.YELLOW)

                    }

                    delete.setOnClickListener {

                        MoEInboxHelper.getInstance().deleteMessage(itemView.context, message)

                        //call interface for delete
                        onDeleteClickListener.onDeleteClicked(position)

                    }


                }


            } catch (e: Exception) {
                Log.i("error", e.toString())
            }

        }
    }

    interface OnDeleteClickListener {
        fun onDeleteClicked(position: Int)
    }



}