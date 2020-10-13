package com.care.app

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var radius=100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        radius=getSharedPreferences("MAIN", Context.MODE_PRIVATE).getInt("RADIUS",100)

    }
    private fun drawCircle(point: LatLng) {
        val circleOptions = CircleOptions()
        circleOptions.center(point)
        circleOptions.radius(radius.toDouble())
        circleOptions.strokeColor(Color.BLACK)
        circleOptions.fillColor(resources.getColor(R.color.tint))
        circleOptions.strokeWidth(2f)
        mMap.addCircle(circleOptions)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val viewModel=ViewModelProviders.of(this).get(ViewModelData::class.java)
        var lon:Double=0.0
        var lat:Double=0.0
        val ism = LatLng(23.814110,86.441207)
        viewModel.getLong().observe(this, Observer { longitude->
            mMap.clear()
            lon=longitude
            drawCircle(ism)
            mMap.addMarker(MarkerOptions().position(LatLng(lat,longitude)).title("Location"))
            mMap.addMarker(MarkerOptions().position(ism).title("Reference"))})
        viewModel.getLat().observe(this, Observer { latitude->
            mMap.clear()
            lat=latitude
            drawCircle(ism)
            mMap.addMarker(MarkerOptions().position(LatLng(latitude,lon)).title("Location"))
            mMap.addMarker(MarkerOptions().position(ism).title("Reference"))})
        val width1 = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width1 * 0.12).toInt()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat,lon)))
        val update = CameraUpdateFactory.newLatLngBounds(LatLngBounds(LatLng(23.809756, 86.433533), LatLng(23.820778, 86.449679)), width1, height, padding)
        mMap.moveCamera(update)
        drawCircle(ism)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==android.R.id.home)
            onBackPressed()
        return true
    }
}
