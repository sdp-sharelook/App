package com.github.sdpsharelook

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.github.sdpsharelook.section.Section
import com.github.sdpsharelook.storage.IRepository
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment : MapsFragmentLift()

var sectionList: MutableList<Section> = mutableListOf()
var wordList: MutableList<Word> = mutableListOf()

open class MapsFragmentLift : Fragment(R.layout.fragment_maps) {

    private var markerMap: HashMap<Marker?, Word> = HashMap<Marker?, Word>()

    @Inject
    lateinit var wordRepos: IRepository<List<Word>>

    @Inject
    lateinit var sectionDb: IRepository<List<Section>>

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
        wordList.clear()
        lifecycleScope.launch {
            sectionDb.flow().collect {
                when {
                    it.isSuccess -> {
                        sectionList = it.getOrDefault(emptyList())!!.toMutableList()
                        val wordList = emptyList<Word>().toMutableList()
                        for (section in sectionList) {
                            wordRepos.flow(section.id).collect { wordFlow ->
                                when {
                                    wordFlow.isSuccess -> {
                                        wordList.addAll(wordFlow.getOrDefault(emptyList()) as MutableList<Word>)
                                        addMarkers(googleMap, wordList)
                                    }
                                    wordFlow.isFailure -> {
                                        wordFlow.exceptionOrNull()
                                    }
                                }
                            }
                        }
                    }
                    it.isFailure -> {
                        it.exceptionOrNull()
                    }
                }
            }
        }
        openImage(googleMap)
    }

    private fun addMarkers(googleMap: GoogleMap, wordList: List<Word>) {
        markerMap.forEach { entry ->
            entry.key?.remove()
        }
        markerMap.clear()
        for (word in wordList) {
            if (word.location != null) {
                val position =
                    LatLng(word.location!!.latitude, word.location!!.longitude)
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

    private fun openImage(map: GoogleMap) {
        map.setOnMarkerClickListener { marker ->
            val word = markerMap[marker]
            BitmapFactory.decodeResource(requireContext().resources, R.drawable.default_user_path)
            val imageView : ImageView = ImageView(requireContext())
            if (word?.picture != null) {
                imageView.load(word.picture)
            } else {
                imageView.setImageResource(R.drawable.ic_no_image)
            }
            if (word != null) {
                val dateArg : Instant
                if (word.savedDate == null) {
                    dateArg = Clock.System.now()
                } else {
                    dateArg = word.savedDate
                }
                ImagePopupFragment.newInstance(
                    word.source.toString(),
                    word.target.toString(),
                    dateArg,
                    imageView.drawable.toBitmap()
                ).show(childFragmentManager, ImagePopupFragment.TAG)

            }
            marker.showInfoWindow()
            true
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.timeline_button, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}