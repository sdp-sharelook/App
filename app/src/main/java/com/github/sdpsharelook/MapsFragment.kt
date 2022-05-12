package com.github.sdpsharelook

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Base64.*
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.sdpsharelook.language.Language
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*

class MapsFragment : Fragment(R.layout.fragment_maps) {

    private var hashMap: HashMap<Marker?, Word> = HashMap<Marker?, Word>()

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    private val callback = OnMapReadyCallback {
        var words = listOf<Word>()

        // ===================================================================================
        // Just for demo purpose
        val pic1 = BitmapFactory.decodeResource(resources, R.drawable.download)
        val date1 = Date(1651810934000)
        val loc1 = Location("loc1") //.setLatitude(46.524107017942995), 6.560875856078083
        loc1.latitude = 46.524107017942995
        loc1.longitude = 6.560875856078083
        val pic2 = BitmapFactory.decodeResource(resources, R.drawable.downloadp)
        val date2 = Date(1604722934000)
        val loc2 = Location("loc2")
        loc2.latitude = 41.90706398797125
        loc2.longitude = 12.48717493069279
        val pic3 = BitmapFactory.decodeResource(resources, R.drawable.downloadpp)
        val date3 = Date(1455337334000)
        val loc3 = Location("loc3")
        loc3.latitude = 51.22670535104726
        loc3.longitude = 4.428296155416405
        val por = Word("1", "Guten Tag", Language("German"), "Bonjour", Language("Francais"), loc1, date1, encodeImage(pic1))
        val por2 = Word("2", "Ciao", Language("Italian"), "Bonjour", Language("Francais"), loc2, date2, encodeImage(pic2))
        val por3 = Word("3", "Hallo", Language("Dutch"), "Bonjour", Language("Francais"), loc3, date3, encodeImage(pic3))
        words = words.plus(por).plus(por2).plus(por3)
        // Just for demo purpose
        // ===========================================================================================================
        // TODO: Fill words with real list of words in the database
        // TODO: Check why Picture is a String and not a Bitmap in Word

        val sydney = LatLng(46.51887158482739, 6.56380824248961)
        it.addMarker(MarkerOptions().position(sydney).title("Marker in Inf"))
        it.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
        setMapLongClick(it)
        openImage(it)
        //setPoiClick(googleMap)
        for (word in words) {
            if (word.location != null) {
                val position = LatLng(word.location.latitude, word.location.longitude)
                val snippet = String.format(
                    Locale.getDefault(),
                    "Lat: %1$.5f, Long: %2$.5f",
                    position.latitude,
                    position.longitude
                )
                val marker = it.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(
                            String.format(
                                Locale.getDefault(),
                                word.source + " : " + word.savedDate.toString()
                            )
                        )
                        .snippet(snippet)
                )
                hashMap[marker] = word
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        lifecycleScope.launch {
            mapFragment?.getMapAsync { callback }
        }
    }

    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->
            // A snippet is additional text that's displayed after the title
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Dropped Pin")
                    .snippet(snippet)
            )
        }
    }

    private fun openImage(map: GoogleMap) {
        map.setOnMarkerClickListener { marker ->
            val word = hashMap[marker]
            BitmapFactory.decodeResource(requireContext().resources, R.drawable.default_user_path)
            if (word != null) {
                ImagePopupFragment.newInstance(
                    word.source.toString(),
                    word.target.toString(),
                    word.savedDate!!,
                    decodeImage(word.picture!!)
                ).show(childFragmentManager, ImagePopupFragment.TAG)

            }
            marker.showInfoWindow()
            true
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return encodeToString(stream.toByteArray(), DEFAULT)
    }

    private fun decodeImage(s: String): Bitmap {
        val p = decode(s, DEFAULT)
        return BitmapFactory.decodeByteArray(p, 0, p.size)
    }
}