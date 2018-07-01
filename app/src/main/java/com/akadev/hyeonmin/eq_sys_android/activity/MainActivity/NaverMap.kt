package com.akadev.hyeonmin.eq_sys_android.activity.MainActivity

import com.akadev.hyeonmin.eq_sys_android.NaverMapTool.PoiFlag
import com.akadev.hyeonmin.eq_sys_android.NaverMapTool.ResourceProvider
import com.akadev.hyeonmin.eq_sys_android.R
import com.akadev.hyeonmin.eq_sys_android.util.Const
import com.akadev.hyeonmin.eq_sys_android.util.Singleton
import com.nhn.android.maps.NMapView
import com.nhn.android.maps.maplib.NGeoPoint
import com.nhn.android.maps.overlay.NMapCircleData
import com.nhn.android.maps.overlay.NMapCircleStyle
import com.nhn.android.maps.overlay.NMapPOIdata
import com.nhn.android.maps.overlay.NMapPathLineStyle
import com.nhn.android.mapviewer.overlay.NMapOverlayManager
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay

class NaverMap(val atvt: MainActivity) {

    var mapView: NMapView = atvt.findViewById(R.id.map_view)
    var mapController = mapView.mapController
    var resourceProvider = ResourceProvider(atvt)
    var overlayManager = NMapOverlayManager(atvt, mapView, resourceProvider)

    var strPoiData = NMapPOIdata(1000, resourceProvider)
    var strPoiDataOverlay: NMapPOIdataOverlay? = null

    var eqCircleData: NMapCircleData? = null
    var eqCircleStyle = NMapCircleStyle(atvt)
    var eqCircleDataOverlay: NMapPathDataOverlay? = null

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
    }

    fun showStructures () {
        strPoiData.beginPOIdata(0)
        strPoiData.removeAllPOIdata()
        atvt.structures?.map {
            val pos = NGeoPoint(it["longitude"]!!.toDouble(), it["latitude"]!!.toDouble())
            var markerString = it["str_name"].toString()
            var marker = when (it["on_team"]!!.toInt()) {
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
            strPoiData?.addPOIitem(pos, markerString, marker, "")
        }
        strPoiData.endPOIdata()
        setStrPoiDataOverlay()
    }

    fun setStrPoiDataOverlay() {
        if (strPoiDataOverlay != null) {
            overlayManager.removeOverlay(strPoiDataOverlay)
        }
        strPoiDataOverlay = overlayManager.createPOIdataOverlay(strPoiData, null)
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
    }

}