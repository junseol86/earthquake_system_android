package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.widget.Button
import com.akadev.hyeonmin.eq_sys_android.NaverMapTool.PoiFlag
import com.akadev.hyeonmin.eq_sys_android.NaverMapTool.ResourceProvider
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.akadev.hyeonmin.eq_sys_android.util.Tool
import com.nhn.android.maps.NMapView
import com.nhn.android.maps.maplib.NGeoPoint
import com.nhn.android.maps.overlay.*
import com.nhn.android.mapviewer.overlay.NMapOverlayManager
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay

class NaverMap(val atvt: MainActivity) {

    var mapView: NMapView = atvt.findViewById(R.id.map_view)
    var mapController = mapView.mapController
    var resourceProvider = ResourceProvider(atvt)
    var overlayManager = NMapOverlayManager(atvt, mapView, resourceProvider)

    var myLocBtn: Button = atvt.findViewById(R.id.my_loc_btn)

    var strPoiData = NMapPOIdata(1000, resourceProvider)
    var strPoiDataOverlay: NMapPOIdataOverlay? = null

    var strReqPoiData = NMapPOIdata(1000, resourceProvider)
    var strReqPoiDataOverlay: NMapPOIdataOverlay? = null

    var sptPoiData = NMapPOIdata(1000, resourceProvider)
    var sptPoiDataOverlay: NMapPOIdataOverlay? = null

    var myPoiData = NMapPOIdata(10, resourceProvider)
    var myPoiDataOverlay: NMapPOIdataOverlay? = null

    var memberPoiData = NMapPOIdata(1000, resourceProvider)
    var memberPoiDataOverlay: NMapPOIdataOverlay? = null

    var eqCircleData: NMapCircleData? = null
    var eqCircleStyle = NMapCircleStyle(atvt)
    var eqCircleDataOverlay: NMapPathDataOverlay? = null
    var eqPoiData = NMapPOIdata(10, resourceProvider)
    var eqPoiDataOverlay: NMapPOIdataOverlay? = null

    init {
        mapView.setClientId(Const.clientId)
        mapView.isClickable = true
        mapView.isEnabled = true
        mapView.isFocusable = true
        mapView.isFocusableInTouchMode = true
        mapView.requestFocus()
        mapView.setScalingFactor(2.5f, true)

        mapController.zoomLevel = 5
        mapController.mapCenter = NGeoPoint(Const.initLng, Const.initLat)

        atvt.earthquakeGetList()

        myLocBtn.setOnClickListener {
            if (Singleton.myLoc != null) {
                mapController.mapCenter = NGeoPoint(Singleton.myLoc!!.longitude, Singleton.myLoc!!.latitude)
            }
        }
    }

    fun showMyPosition (lat: Double, lng: Double) {
        myPoiData.beginPOIdata(0)
        myPoiData.removeAllPOIdata()
        myPoiData.addPOIitem(NGeoPoint(lng, lat), "내 위치", PoiFlag.MEMBER_I, "")
        myPoiData.endPOIdata()
        setMyPoiDataOverlay()
    }

    fun setMyPoiDataOverlay() {
        if (myPoiDataOverlay != null) {
            overlayManager.removeOverlay(myPoiDataOverlay)
        }
        myPoiDataOverlay = overlayManager.createPOIdataOverlay(myPoiData, null)
    }

    fun showMemberPosition () {
        memberPoiData.beginPOIdata(0)
        memberPoiData.removeAllPOIdata()

        atvt.members?.map {

            if (it["mbr_idx"] != Singleton.memberInfo!!["mbr_idx"] &&
                    Tool.isReportedToday(it["mbr_pos_last_report"])) {
                val pos = NGeoPoint(it["longitude"]!!.toDouble(), it["latitude"]!!.toDouble())
                var markerString = "[${if (it["mbr_team"].toString() == "0") "미편성" else it["mbr_team"].toString()}] ${it["mbr_name"]}"
                val mkr = if (Tool.isReportedToday(it["mbr_arr_last_report"])) {
                    if (it["mbr_arrive_in"] != "-1")
                        PoiFlag.MEMBER_POS
                    else
                        PoiFlag.MEMBER_REJECT
                } else {
                    PoiFlag.MEMBER
                }
                memberPoiData.addPOIitem(pos, markerString, mkr, it["mbr_phone"])

            }

        }

        myPoiData.endPOIdata()
        setMemberPoiDataOverlay()
    }

    fun setMemberPoiDataOverlay() {
        if (memberPoiDataOverlay != null) {
            overlayManager.removeOverlay(memberPoiDataOverlay)
        }
        memberPoiDataOverlay = overlayManager.createPOIdataOverlay(memberPoiData, null)
        memberPoiDataOverlay?.onStateChangeListener = object: NMapPOIdataOverlay.OnStateChangeListener {
            override fun onFocusChanged(p0: NMapPOIdataOverlay?, p1: NMapPOIitem?) {
            }
            override fun onCalloutClick(p0: NMapPOIdataOverlay?, p1: NMapPOIitem?) {

                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${p1!!.tag}")
                if (ContextCompat.checkSelfPermission(atvt, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    atvt.startActivity(intent)
                } else {
                    atvt.ac?.askCallPermission()
                }
            }
        }
    }

//    일반 구조물들
    fun showStructures () {
        strPoiData.beginPOIdata(0)
        strPoiData.removeAllPOIdata()
        atvt.structures?.map {
            val pos = NGeoPoint(it["longitude"]!!.toDouble(), it["latitude"]!!.toDouble())
            val markerString = it["str_name"].toString()
            val marker = when (it["on_team"]!!.toInt()) {
                1 -> PoiFlag.TM_1
                2 -> PoiFlag.TM_2
                3 -> PoiFlag.TM_3
                4 -> PoiFlag.TM_4
                5 -> PoiFlag.TM_5
                6 -> PoiFlag.TM_6
                7 -> PoiFlag.TM_7
                8 -> PoiFlag.TM_8
                else -> PoiFlag.TM_0
            }
            strPoiData.addPOIitem(pos, markerString, marker, it)
        }
        strPoiData.endPOIdata()
        setStrPoiDataOverlay()
        showStructuresRequested()
    }

    fun setStrPoiDataOverlay() {
        if (strPoiDataOverlay != null) {
            overlayManager.removeOverlay(strPoiDataOverlay)
        }
        strPoiDataOverlay = overlayManager.createPOIdataOverlay(strPoiData, null)
        strPoiDataOverlay?.onStateChangeListener = object : NMapPOIdataOverlay.OnStateChangeListener {
            override fun onFocusChanged(p0: NMapPOIdataOverlay?, p1: NMapPOIitem?) {
            }
            override fun onCalloutClick(p0: NMapPOIdataOverlay?, p1: NMapPOIitem?) {
                atvt.sr?.onClickStrBalloon(p1!!)
            }
        }
    }

//    점검 요망 구조물들
    fun showStructuresRequested () {
        strReqPoiData.beginPOIdata(0)
        strReqPoiData.removeAllPOIdata()
        atvt.structures?.map {
            if (it["str_need_check"]!!.toInt() == 1) {
                val pos = NGeoPoint(it["longitude"]!!.toDouble(), it["latitude"]!!.toDouble())
                val markerString = it["str_name"].toString()
                val marker = PoiFlag.REQ
                strReqPoiData.addPOIitem(pos, markerString, marker, it)
            }
        }
        strReqPoiData.endPOIdata()
        setStrReqPoiDataOverlay()
    }

    fun setStrReqPoiDataOverlay() {
        if (strReqPoiDataOverlay != null) {
            overlayManager.removeOverlay(strReqPoiDataOverlay)
        }
        strReqPoiDataOverlay = overlayManager.createPOIdataOverlay(strReqPoiData, null)
        strReqPoiDataOverlay?.onStateChangeListener = object : NMapPOIdataOverlay.OnStateChangeListener {
            override fun onFocusChanged(p0: NMapPOIdataOverlay?, p1: NMapPOIitem?) {
            }
            override fun onCalloutClick(p0: NMapPOIdataOverlay?, p1: NMapPOIitem?) {
                atvt.sr?.onClickStrBalloon(p1!!)
            }
        }
    }

    fun showSpots () {
        sptPoiData.beginPOIdata(0)
        sptPoiData.removeAllPOIdata()

        atvt.spots?.map {
            val pos = NGeoPoint(it["longitude"]!!.toDouble(), it["latitude"]!!.toDouble())
            val markerString = it["sp_name"].toString()
            val marker = when (it["sp_type"]) {
                "branch" -> PoiFlag.BRANCH
                "ic" -> PoiFlag.IC
                else -> PoiFlag.JC
            }
            sptPoiData.addPOIitem(pos, markerString, marker, "")
        }

        sptPoiData.endPOIdata()
        setSptPoiDataOverlay()
    }
    fun setSptPoiDataOverlay () {
        if (sptPoiDataOverlay != null) {
            overlayManager.removeOverlay(sptPoiDataOverlay)
        }
        sptPoiDataOverlay = overlayManager.createPOIdataOverlay(sptPoiData, null)
    }

    fun drawEqCircle () {
        eqCircleData = NMapCircleData(0)
        if (Singleton.earthquakeInfo != null) {
            eqCircleData?.initCircleData()
            val eqInf = Singleton.earthquakeInfo!!
            eqCircleData?.addCirclePoint(eqInf["longitude"]!!.toDouble(), eqInf["latitude"]!!.toDouble(), 25000f * (eqInf["eq_level"]!!.toFloat() + 1f))
            eqCircleData?.endCircleData()
            val colors = arrayOf(2469198, 5405929, 16734501)
            eqCircleStyle.setLineType(NMapPathLineStyle.TYPE_SOLID)
            eqCircleStyle.setStrokeColor(colors[eqInf["eq_level"]!!.toInt()], 200)
            eqCircleStyle.setStrokeWidth(2f)
            eqCircleStyle.setFillColor(colors[eqInf["eq_level"]!!.toInt()], 50)
            eqCircleData?.circleStyle = eqCircleStyle

        }
        setCircleDataOverlay()
    }

    fun setCircleDataOverlay() {
        if (eqCircleDataOverlay != null) {
            overlayManager.removeOverlay(eqCircleDataOverlay)
        }
        eqCircleDataOverlay = overlayManager.createPathDataOverlay()
        eqCircleDataOverlay?.addCircleData(eqCircleData)
        eqCircleDataOverlay?.showAllPathData(4)
        showEq()
    }

    fun showEq () {
        eqPoiData.beginPOIdata(0)
        eqPoiData.removeAllPOIdata()
        if (Singleton.earthquakeInfo != null) {
            val eqInf = Singleton.earthquakeInfo!!
            val pos = NGeoPoint(eqInf["longitude"]!!.toDouble(), eqInf["latitude"]!!.toDouble())
            var marker = when (eqInf["eq_level"]!!.toInt()) {
                0 -> PoiFlag.EQ_25
                1 -> PoiFlag.EQ_50
                else -> PoiFlag.EQ_100
            }
            var markerString = when (eqInf["eq_level"]!!.toInt()) {
                0 -> "자체대응"
                1 -> "대응 1단계"
                else -> "대응 2단계"
            }
            eqPoiData.addPOIitem(pos, markerString, marker, "")
        }
        eqPoiData.endPOIdata()
        showEqPoiDataOverlay()
    }
    fun showEqPoiDataOverlay () {
        if (eqPoiDataOverlay != null) {
            overlayManager.removeOverlay(eqPoiDataOverlay)
        }
        eqPoiDataOverlay = overlayManager.createPOIdataOverlay(eqPoiData, null)
    }

}