package com.dicoding.storyappsubmission.view.storyMaps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyappsubmission.R
import com.dicoding.storyappsubmission.data.ResultState
import com.dicoding.storyappsubmission.data.remote.response.ListStoryItem
import com.dicoding.storyappsubmission.databinding.ActivityStoryMapsBinding
import com.dicoding.storyappsubmission.utils.setBackgroundActionBar
import com.dicoding.storyappsubmission.utils.setLocalDateFormat
import com.dicoding.storyappsubmission.utils.showToast
import com.dicoding.storyappsubmission.view.factory.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class StoryMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapsBinding

    private lateinit var viewModel: StoryMapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBackgroundActionBar(this@StoryMapsActivity)
        supportActionBar?.title = getString(R.string.snap_story_maps)
        supportActionBar?.show()

        val viewModelFactory = ViewModelFactory.getInstance(this@StoryMapsActivity)
        viewModel = ViewModelProvider(this, viewModelFactory)[StoryMapsViewModel::class.java]

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
        mMap = googleMap
        setMapsHelper()

        viewModel.getAllStoriesWithLocation().observe(this) { resultState ->
            if (resultState != null) {
                when (resultState) {
                    is ResultState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        lifecycleScope.launch {
                            setAllStoryMapsMarker(resultState.data.listStory)
                        }
                    }
                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        showToast(this@StoryMapsActivity, resultState.errorMessage)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setAllStoryMapsMarker(listStory: List<ListStoryItem>) {
        listStory.forEach { story ->
            if (story.lat != null && story.lon != null) {
                val latLng = LatLng(story.lat, story.lon)
                setMarkerLocation(latLng, story.name, setLocalDateFormat(story.createdAt))
            }
        }
    }

    private fun setMapsHelper() {
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun setMarkerLocation(latLng: LatLng, title: String, snippet: String) {
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
                .snippet(snippet)
        )
        mMap.moveCamera(
            CameraUpdateFactory.newLatLng(latLng)
        )
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 5f)
        )
    }

    companion object {
        private const val TAG = "SortMapsActivity"
    }
}