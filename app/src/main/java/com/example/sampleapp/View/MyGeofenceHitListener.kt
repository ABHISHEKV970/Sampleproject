package com.example.sampleapp.View

import com.moengage.geofence.listener.OnGeofenceHitListener
import com.moengage.geofence.model.GeofenceData

class MyGeofenceHitListener : OnGeofenceHitListener {


    override fun geofenceHit(geofenceData: GeofenceData): Boolean {

        print(geofenceData)

        return true
    }
}