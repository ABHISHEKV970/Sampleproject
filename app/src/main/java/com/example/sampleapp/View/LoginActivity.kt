package com.example.sampleapp.View

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.sampleapp.R
import com.example.sampleapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.geofence.MoEGeofenceHelper
import com.moengage.pushbase.MoEPushHelper
import java.text.SimpleDateFormat
import java.util.Date


class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    private val helper = Helper()

    private val ALARM_PERMISSION_REQUEST_CODE = 100
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val POST_NOTIFICATIONS_REQUEST_CODE = 101

    private lateinit var sharedPref: SharedPreferences

    private var push_permission_count: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)


        sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        //Grant geofence permission

        //geofence_check()

        //push template [Timer with progressbard]
        check_alarm_permission();

        //moengage asking for request push notification above andorid 13+ devices
        //MoEPushHelper.getInstance().requestPushPermission(this)

        //Custom function for requesting push notification above andorid 13+ devices
        CustomPushNotification()


        binding.loginButton.setOnClickListener {

//            val unique_Id = "Test_" + getCurrentTimestamp()

            val unique_Id = "Test_" + "9448"

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


    private fun CustomPushNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                //Call Notification Channel creating API to moengage
                MoEPushHelper.getInstance().setUpNotificationChannels(this)

            } else {

                try {
                    push_permission_count = sharedPref.getInt("pushpermission",0)
                }
                catch (e:Exception)
                {
                    Log.i("exception",e.toString())
                }

                val editor = sharedPref.edit()
                editor.putInt("pushpermission", push_permission_count + 1)
                editor.apply()

                // Update the permission request count in the SDK
                var pass_push_permission_count = sharedPref.getInt("pushpermission",0)

                Log.i("countval",pass_push_permission_count.toString());

                MoEPushHelper.getInstance().updatePushPermissionRequestCount(applicationContext, pass_push_permission_count)

                if(pass_push_permission_count<=2)
                {
                   // requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.POST_NOTIFICATIONS), LOCATION_PERMISSION_REQUEST_CODE)
                }
                else
                {
                    check_user_concent()
                }

            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()

            //Notified SDK about the notification result.
            MoEPushHelper.getInstance().pushPermissionResponse(applicationContext, isGranted)
        }
        else {

            check_user_concent()
        }
    }


    private fun check_user_concent() {

        val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Sample App")
        alertDialogBuilder.setMessage("Requesting to turn on notification in settings")
        alertDialogBuilder.setCancelable(true)


        alertDialogBuilder.setPositiveButton("Go to settings") { _, _ ->

            //Navigate to settings page to turn on notification
            MoEPushHelper.getInstance().navigateToSettings(this)

        }

        val alertDialog: androidx.appcompat.app.AlertDialog = alertDialogBuilder.create()
        alertDialog.show()



    }

    private fun check_alarm_permission() {

        val androidVersion = Build.VERSION.SDK_INT
        if (androidVersion >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (!checkAlarmPermission()) {
                requestAlarmPermission()
            }
        } else {
        }

    }

    private fun checkAlarmPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.SET_ALARM
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAlarmPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.SET_ALARM),
            ALARM_PERMISSION_REQUEST_CODE
        )
    }

    fun getCurrentTimestamp(): String {
        val dateFormat = SimpleDateFormat("dd:MM:yyyy HH:mm:ss")
        val currentTime = Date()
        return dateFormat.format(currentTime)
    }

    private fun geofence_check() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Fine Location permission is granted
            // Check if current android version >= 10, if >= 10 check for Background Location permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    println("Granted Permission !")

                } else {
                    // Ask for Background Location Permission
                    askPermissionForBackgroundUsage();
                }
            }
        } else {
            // Fine Location Permission is not granted so ask for permission
            askForLocationPermission();
        }

    }

    private fun askForLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Permission Needed!")
                .setMessage("Location Permission Needed!")
                .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                })
                .setNegativeButton("CANCEL", DialogInterface.OnClickListener { dialog, which ->
                    println("Denied Permission !")
                })
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun askPermissionForBackgroundUsage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Permission Needed!")
                .setMessage("Background Location Permission Needed!, tap \"Allow all time in the next screen\"")
                .setPositiveButton("OK") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf<String>(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                }
                .setNegativeButton(
                    "CANCEL"
                ) { dialog, which ->
                    println("Denied Permission !")
                }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now check if android version >= 11, if >= 11 check for Background Location Permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //Background Location Permission is granted so do your work here
                        println("permission granted")

                    } else {
                        // Ask for Background Location Permission
                        askPermissionForBackgroundUsage();
                    }
                } else {

                }
            } else {
                // User denied location permission
            }
        }
        else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // User granted for Background Location Permission.
                println("permission granted")
            } else {
                // User declined for Background Location Permission.
                println("permission declined")

            }
        }
        else if (requestCode == ALARM_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Called the timer with progressbar alarm
                startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))

            } else {
                println("Permission denied")
            }
        }

        else if (requestCode == POST_NOTIFICATIONS_REQUEST_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
                    //Notified SDK about the notification result.
                    MoEPushHelper.getInstance().pushPermissionResponse(applicationContext, true)

            } else {
                check_user_concent()
                println("Permission denied")
            }
        }


        else {
            //add some logics at here
        }
    }





}