package com.exampleapp.ui.maps

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exampleapp.R
import com.exampleapp.utils.Logger
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.jetbrains.anko.locationManager


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var polygon: Polygon? = null

    private var userLocationMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //geofence
        val latLngList = arrayListOf(
            LatLng(-6.95592447930949, 107.59289790192844),
            LatLng(-6.9561777772030995, 107.59282772848613),
            LatLng(-6.956232394543495, 107.59311320680827),
            LatLng(-6.956137958737095, 107.5931259381964),
            LatLng(-6.956113996545716, 107.59304278972009),
            LatLng(-6.95600217296974, 107.59307497622704)
        )

        val polygonOptions = PolygonOptions().addAll(latLngList)
        polygon = mMap.addPolygon(polygonOptions!!)
        polygon?.strokeColor = Color.argb(60, 224, 0, 0)
        polygon?.fillColor = Color.argb(50, 224, 0, 0)

        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location != null) {
            val userLatLng = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(userLatLng))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 30f))

            Logger.print(pointInPolygon(userLatLng, polygon!!).toString())

        }

    }

    private fun pointInPolygon(point: LatLng, polygon: Polygon): Boolean {
        // ray casting alogrithm http://rosettacode.org/wiki/Ray-casting_algorithm
        var crossings = 0
        val path = polygon.points
        path.removeAt(path.size - 1) //remove the last point that is added automatically by getPoints()

        // for each edge
        for (i in path.indices) {
            val a = path[i]
            var j = i + 1
            //to close the last edge, you have to take the first point of your polygon
            if (j >= path.size) {
                j = 0
            }
            val b = path[j]
            if (rayCrossesSegment(point, a, b)) {
                crossings++
            }
        }

        // odd number of crossings?
        return crossings % 2 == 1
    }

    private fun rayCrossesSegment(point: LatLng, a: LatLng, b: LatLng): Boolean {

        // Ray Casting algorithm checks, for each segment, if the point is 1) to the left of the segment and 2) not above nor below the segment. If these two conditions are met, it returns true

        // Ray Casting algorithm checks, for each segment, if the point is 1) to the left of the segment and 2) not above nor below the segment. If these two conditions are met, it returns true
        var px = point.longitude
        var py = point.latitude
        var ax: Double = a.longitude
        var ay: Double = a.latitude
        var bx: Double = b.longitude
        var by: Double = b.latitude
        if (ay > by) {
            ax = b.longitude
            ay = b.latitude
            bx = a.longitude
            by = a.latitude
        }
        // alter longitude to cater for 180 degree crossings
        // alter longitude to cater for 180 degree crossings
        if (px < 0 || ax < 0 || bx < 0) {
            px += 360.0
            ax += 360.0
            bx += 360.0
        }
        // if the point has the same latitude as a or b, increase slightly py
        // if the point has the same latitude as a or b, increase slightly py
        if (py == ay || py == by) py += 0.00000001


        // if the point is above, below or to the right of the segment, it returns false


        // if the point is above, below or to the right of the segment, it returns false
        return if (py > by || py < ay || px > Math.max(ax, bx)) {
            false
        } else if (px < Math.min(ax, bx)) {
            true
        } else {
            val red = if (ax != bx) (by - ay) / (bx - ax) else Double.POSITIVE_INFINITY
            val blue = if (ax != px) (py - ay) / (px - ax) else Double.POSITIVE_INFINITY
            blue >= red
        }
    }
}