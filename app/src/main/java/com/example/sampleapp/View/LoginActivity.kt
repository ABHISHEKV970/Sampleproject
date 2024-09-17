package com.example.sampleapp.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.sampleapp.R
import com.example.sampleapp.databinding.ActivityLoginBinding
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.pushbase.MoEPushHelper
import java.text.SimpleDateFormat
import java.util.Date

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    private val helper = Helper()

    private  val requestCode = 123;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)


        binding.loginButton.setOnClickListener {


            val unique_Id = "Test_" + getCurrentTimestamp()

            if (helper.checkForInternet(this)) {

                MoEAnalyticsHelper.setUniqueId(this, unique_Id)

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "No Internet Connection Found !", Toast.LENGTH_SHORT).show()

            }




        }


        binding.settings.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


    }

    fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("dd:MM:yyyy HH:mm:ss")
        val currentTime = Date()
        return dateFormat.format(currentTime)
    }


}