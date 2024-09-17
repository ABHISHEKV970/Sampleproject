package com.example.sampleapp.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.sampleapp.R
import com.example.sampleapp.databinding.ActivitySignUpBinding
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.disableAdIdTracking
import com.moengage.core.disableAndroidIdTracking
import com.moengage.core.enableAdIdTracking
import com.moengage.core.enableAndroidIdTracking
import com.moengage.pushbase.MoEPushHelper

class SignUpActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        //For handling custom notification click
        MoEPushHelper.getInstance().logNotificationClick(applicationContext, intent)


        try {
            //TASK 2 USING SHARED PREFERENCE

            val storedValue = sharedPref.getBoolean("notificationstatus",true)

            if(storedValue)
            {
                binding.switchButtonNotify.isChecked = true
                binding.notificationText.setText("ON")


            }
            else
            {
                binding.switchButtonNotify.isChecked = false

                binding.notificationText.setText("OFF")


            }
        }
        catch (e : Exception)
        {
            Log.i("error",e.toString())
        }



        binding.signupButton.setOnClickListener {
            MoEAnalyticsHelper.setFirstName(this,binding.firstName.text.toString())
            MoEAnalyticsHelper.setLastName(this,binding.lastName.text.toString())
            MoEAnalyticsHelper.setUserName(this,binding.userName.text.toString())
            MoEAnalyticsHelper.setMobileNumber(this,binding.mobileNo.text.toString())
            MoEAnalyticsHelper.setEmailId(this,binding.edEmail.text.toString())

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->

            if(isChecked)
            {
                check_user_concent("Are you sure you want to track your device Android ID ?"){ result ->
                    if(result)
                    {
                        binding.status.setText("ON")
                        enableAndroidIdTracking(this)
                    }
                    else
                    {
                        binding.switchButton.isChecked = false
                        binding.status.setText("OFF")
                    }
                }

            }
            else
            {
                binding.status.setText("OFF")
                binding.switchButton.isChecked = false
                disableAndroidIdTracking(this)
            }

        }

        binding.switchButtonAdvId.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
            {
                check_user_concent("Are you sure you want to track your device Advertisement ID ?"){ result ->
                    if(result)
                    {
                        binding.statusAdvertisement.setText("ON")
                        enableAdIdTracking(this)
                    }
                    else
                    {
                        binding.switchButtonAdvId.isChecked = false
                        binding.statusAdvertisement.setText("OFF")
                    }
                }

            }
            else
            {
                binding.statusAdvertisement.setText("OFF")
                binding.switchButtonAdvId.isChecked = false
                disableAdIdTracking(this)
            }

        }



        //TASK 2 USING SHARED PREFERENCE

        binding.switchButtonNotify.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked)
            {

                binding.notificationText.setText("ON")

                val valueToStore = true

                val editor = sharedPref.edit()
                editor.putBoolean("notificationstatus", valueToStore)
                editor.apply()

            }
            else
            {
                binding.notificationText.setText("OFF")
                val valueToStore = false
                val editor = sharedPref.edit()
                editor.putBoolean("notificationstatus", valueToStore)
                editor.apply()
            }

        }



    }

    private fun check_user_concent(title: String, status: (Boolean) -> Unit) {

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Confirmation")
        alertDialogBuilder.setMessage(title)
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setNegativeButton("Cancel") { _, _ ->
            status(false)
        }

        alertDialogBuilder.setPositiveButton("Confirm") { _, _ ->
            status(true)
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()



    }


}