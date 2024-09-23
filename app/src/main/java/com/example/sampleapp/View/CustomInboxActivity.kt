package com.example.sampleapp.View

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapp.Models.NotifyData
import com.example.sampleapp.R
import com.example.sampleapp.databinding.ActivityCustomInboxBinding
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.core.model.InboxData
import com.moengage.inbox.core.model.InboxMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CustomInboxActivity : AppCompatActivity(),AppInboxAdapter.OnDeleteClickListener
{
    private lateinit var adapter: AppInboxAdapter

    private lateinit var binding: ActivityCustomInboxBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_inbox)


        binding.notifyRv.layoutManager = LinearLayoutManager(this)
        adapter = AppInboxAdapter(this)

        fetch_msgs()

        binding.backbtn.setOnClickListener{

            finish()
        }

        //delete all
        binding.deleteAllTv.setOnClickListener {

            MoEInboxHelper.getInstance().deleteAllMessages(this)

            fetch_msgs()

        }

    }

    private fun fetch_msgs() {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            // Fetch all messages in a background thread
            MoEInboxHelper.getInstance().fetchAllMessagesAsync(applicationContext, adapter)
            binding.notifyRv.adapter = adapter

        }
    }

    override fun onDeleteClicked(position: Int) {
       fetch_msgs()
    }
}