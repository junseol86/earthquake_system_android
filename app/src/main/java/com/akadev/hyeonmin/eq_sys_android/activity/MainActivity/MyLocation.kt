package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MyLocation(val atvt: MainActivity) {

    val REQ_CODE = 0

    var lMng: LocationManager = atvt.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var lLstn: LocationListener = object: LocationListener {
        override fun onLocationChanged(location: Location?) {
            Singleton.myLoc = location
            if (location != null) {
                atvt.rt?.al?.locationReport(location.latitude, location.longitude)
                println("LOCATION ${location.latitude} ${location.longitude}")
                atvt.nm?.showMyPosition(location.latitude, location.longitude)
            }
            atvt.nm?.myLocBtn?.visibility = if (location != null) View.VISIBLE else View.GONE
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onProviderDisabled(provider: String?) {
        }

    }

    init {


    }

    fun getLocWithPermissionCheck () {
        if (ContextCompat.checkSelfPermission(atvt, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(atvt, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQ_CODE)
        } else {
            lMng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, lLstn)
        }
    }

}