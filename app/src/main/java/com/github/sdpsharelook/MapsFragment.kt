package com.github.sdpsharelook

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64.*
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.github.sdpsharelook.storage.IRepository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {

    private var markerMap: HashMap<Marker?, Word> = HashMap<Marker?, Word>()

    @Inject
    lateinit var wordRepos: IRepository<List<Word>>

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    private val callback = OnMapReadyCallback { googleMap ->
        var words = listOf<Word>()
        lifecycleScope.launch {
            wordRepos.flow().collect { words ->
                val wordList = words.getOrDefault(emptyList<Word>())
                markerMap.forEach { entry ->
                    entry.key?.remove()
                }
                markerMap.clear()
                for (word in wordList!!) {
                    if (word.location != null) {
                        val position = LatLng(word.location!!.latitude, word.location!!.longitude)
                        val snippet = String.format(
                            Locale.getDefault(),
                            "Lat: %1$.5f, Long: %2$.5f",
                            position.latitude,
                            position.longitude
                        )
                        val marker = googleMap.addMarker(
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
                        markerMap[marker] = word
                    }
                }

            }
        }

        setMapLongClick(googleMap)
        openImage(googleMap)
        //setPoiClick(googleMap)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        lifecycleScope.launch {
            mapFragment?.getMapAsync { callback }
            wordRepos.flow().collect {

            }
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
            val word = markerMap[marker]
            BitmapFactory.decodeResource(requireContext().resources, R.drawable.default_user_path)
            val imageView : ImageView = ImageView(requireContext())
            if (word != null) {
                imageView.load(word.picture!!)
            }
            if (word != null) {
                word.savedDate?.let {
                    ImagePopupFragment.newInstance(
                        word.source.toString(),
                        word.target.toString(),
                        it,
                        imageView.drawable.toBitmap()
                        //decodeImage(word.picture!!)
                    ).show(childFragmentManager, ImagePopupFragment.TAG)
                }

            }
            marker.showInfoWindow()
            true
        }
    }

    private fun getWord() {
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


