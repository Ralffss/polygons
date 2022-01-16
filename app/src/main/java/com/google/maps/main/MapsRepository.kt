package com.google.maps.main

import com.google.maps.data.models.FeaturesResponse
import com.google.maps.util.Resource

interface MapsRepository {

    suspend fun getFeatures(): Resource<FeaturesResponse>
}