package com.example.sampleapp.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.sampleapp.R
import com.example.sampleapp.databinding.ActivityLoginBinding
import com.moengage.core.analytics.MoEAnalyticsHelper
import java.text.SimpleDateFormat
import java.util.Date

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)


        binding.loginButton.setOnClickListener {


            val unique_Id = "Test_"+ getCurrentTimestamp()

            Log.i("data",unique_Id)

            MoEAnalyticsHelper.setUniqueId(this, unique_Id)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


        binding.newUser.setOnClickListener{

            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }


    }

    fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("dd:MM:yyyy HH:mm:ss")
        val currentTime = Date()
        return dateFormat.format(currentTime)
    }


}