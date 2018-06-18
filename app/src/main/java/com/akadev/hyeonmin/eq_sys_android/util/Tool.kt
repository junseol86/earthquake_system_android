package com.akadev.hyeonmin.eq_sys_android.util

import com.akadev.hyeonmin.eq_sys_android.R
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import org.json.JSONArray
import org.json.JSONObject

object Tool {

    fun jaToAL(ja: JSONArray): ArrayList<Map<String, String>> {

        var result = ArrayList<Map<String, String>>()

        for (i in 0 until ja.length()) {
            val jo = ja[i] as JSONObject
            var map = HashMap<String, String>()
            for (key in jo.keys()) {
                map[key] = jo[key.toString()].toString()
            }
            result.add(map)
        }

        return result
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515 * 1.609344
        return dist
    }

    fun setIconAndSituation(it: Map<String, String>): BitmapDescriptor {

        var icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_grey)

        if (Singleton.earthquakeInfo != null) {
            val mi = Singleton.memberInfo!!
            val ei = Singleton.earthquakeInfo!!
            val dist = Tool.distance(it["latitude"]!!.toDouble(), it["longitude"]!!.toDouble(), ei["latitude"]!!.toDouble(), ei["longitude"]!!.toDouble())

            if (ei["eq_type"].equals("inland")) {
                if (ei["eq_strength"]!!.toDouble() in 3.5..3.99999) {
                    if (mi["mbr_team"]!!.toInt() <= 2) {
                        if (dist < 25) {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_green)
                        }
                    }
                }
                else if (ei["eq_strength"]!!.toDouble() in 4.0..4.99999) {
                    if (mi["mbr_team"]!!.toInt() <= 4) {
                        if (dist < 50) {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_yellow)
                        }
                    }
                }
                else if (ei["eq_strength"]!!.toDouble() >= 5.0) {
                    if (mi["mbr_team"]!!.toInt() <= 6) {
                        if (dist < 100) {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_red)
                        }
                    }
                    if (mi["mbr_team"]!!.toInt() <= 8) {
                        if (dist >= 100) {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_purple)
                        }
                    }
                }
            } else if (ei["eq_type"].equals("waters")) {
                if (ei["eq_strength"]!!.toDouble() in 4.0..4.49999) {
                    if (mi["mbr_team"]!!.toInt() <= 2) {
                        if (dist < 25) {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_green)
                        }
                    }
                }
                else if (ei["eq_strength"]!!.toDouble() in 4.5..5.49999) {
                    if (mi["mbr_team"]!!.toInt() <= 4) {
                        if (dist < 50) {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_yellow)
                        }
                    }
                }
                else if (ei["eq_strength"]!!.toDouble() >= 5.5) {
                    if (mi["mbr_team"]!!.toInt() <= 6) {
                        if (dist < 100) {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_red)
                        }
                    }
                    if (mi["mbr_team"]!!.toInt() <= 8) {
                        if (dist >= 100) {
                            icon = BitmapDescriptorFactory.fromResource(R.drawable.mark_purple)
                        }
                    }
                }
            }
        }

        return icon
    }

}