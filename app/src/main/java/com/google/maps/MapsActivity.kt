package com.google.maps

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.data.models.Point
import com.google.maps.databinding.ActivityMapsBinding
import com.google.maps.main.MapsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewModel: MapsViewModel by viewModels()
    private val areas = mutableListOf<MutableList<Point>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.featureLiveData.observe(this) {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

            fun addFeaturesToList() {
                var lastIndexFeature = -1
                var lastIndexPoint = -1
                var idFeatureValue = 0

                it.features.forEach { feature ->
                    areas.add(mutableListOf())
                    ++lastIndexFeature
                    ++idFeatureValue
                    feature.points.forEach { point ->
                        ++lastIndexPoint
                        if (feature.id == idFeatureValue) {
                            areas[lastIndexFeature].add(
                                point.copy(
                                    accuracy = point.accuracy,
                                    latitude = point.latitude,
                                    longitude = point.longitude
                                )
                            )
                        }
                    }
                }
            }

            addFeaturesToList()
        }

        viewModel.fetchFeatures()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        drawPolygons()
        addMarkers()
    }

    private fun drawPolygons() {
        val polygons = mutableListOf<PolygonOptions>()
        var lastIndex = -1

        for (area in areas) {
            val transparentRed = 0x33FF0000
            polygons.add(
                PolygonOptions().fillColor(transparentRed).strokeColor(Color.RED).strokeWidth(
                    3F
                )
            )
            ++lastIndex
            for (point in area) {
                polygons[lastIndex].add(
                    LatLng(
                        point.latitude,
                        point.longitude
                    )
                )
            }

            mMap.addPolygon(polygons[lastIndex])
        }
    }

    private fun addMarkers() {
        for (area in areas) {
            for (point in area) {
                if (point.accuracy >= 0.0 && point.accuracy < 1.5)
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN
                            )
                        ).position(LatLng(point.latitude, point.longitude))
                    )
                else if (point.accuracy >= 1.5 && point.accuracy < 2.0)
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_YELLOW
                            )
                        ).position(LatLng(point.latitude, point.longitude))
                    )
                else
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_ORANGE
                            )
                        ).position(LatLng(point.latitude, point.longitude))
                    )
            }
        }
    }
}