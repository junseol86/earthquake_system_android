package com.akadev.hyeonmin.eq_sys_android.activity

import android.os.Bundle
import android.widget.TextView
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.activity.extension.MyCustActivity
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.util.Tool
import com.akadev.hyeonmin.eq_sys_android.volley.Earthquake
import com.akadev.hyeonmin.eq_sys_android.volley.Structure

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.time.format.SignStyle

class MapActivity : MyCustActivity(), OnMapReadyCallback {

    var teamAndName: TextView? = null

    private lateinit var mMap: GoogleMap

    var earthquakeVly: Earthquake? = null

    var structureVly: Structure? = null
    var structures: ArrayList<Map<String, String>>? = null
    var strMarkers = ArrayList<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        teamAndName = findViewById(R.id.team_n_name)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        visMyInfo()

        earthquakeVly = Earthquake(this)
        structureVly = Structure(this)

        earthquakeGetList()
    }

    override fun loginResult(id: String, pw: String, jwtToken: String) {
        super.loginResult(id, pw, jwtToken)
        visMyInfo()
        structureGetList()
    }

    fun visMyInfo() {
        val teamOrg = Singleton.memberInfo!!["mbr_team"]
        val team = if (teamOrg == "0") "조 미배정" else teamOrg
        teamAndName?.text = "[${team}조] ${Singleton.memberInfo!!["mbr_name"]}"
    }

    fun earthquakeGetList() {
        earthquakeVly?.getList()
    }
    fun earthquakeGetListResult(eqList: List<Map<String, String>>) {
        Singleton.earthquakeGetListResult(eqList)

        structureGetList()
    }

    fun structureGetList() {
        structureVly?.getList()
    }
    fun structureGetListResult(str: ArrayList<Map<String, String>>) {
        structures = str
        structures?.map { it ->

            val icon = Tool.setIconAndSituation(it)

            var marker = mMap.addMarker(MarkerOptions()
                    .position(LatLng(
                            it["latitude"]!!.toDouble(),
                            it["longitude"]!!.toDouble()
                    ))
                    .title(it["str_name"])
                    .snippet("${it["str_branch"]} ${it["str_line"]}")
                    .icon(icon))
            strMarkers.add(marker)
        }
    }
}
