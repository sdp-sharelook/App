package com.github.sdpsharelook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.github.sdpsharelook.databinding.ActivityMapsBinding
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
        map = googleMap
        val words = listOf<WordF>()
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(46.51887158482739, 6.56380824248961)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Inf"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
        setMapLongClick(map)
        if (words != null) {
            for (word in words) {
                if (word.location != null) {
                    val position = LatLng(word.location.latitude, word.location.longitude)
                    val snippet = String.format(
                        Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        position.latitude,
                        position.longitude
                    )
                    map.addMarker(MarkerOptions()
                        .position(position)
                        .title(String.format(Locale.getDefault(), word.source + " : " + word.savedDate.toString()))
                        .snippet(snippet))
                }
            }
        }


    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener {
                latLng ->
            // A snippet is additional text that's displayed after the title
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(MarkerOptions()
                .position(latLng)
                .title("Dropped Pin")
                .snippet(snippet))
        }

        map.setOnMarkerClickListener { it ->

            true
        }
    }
}