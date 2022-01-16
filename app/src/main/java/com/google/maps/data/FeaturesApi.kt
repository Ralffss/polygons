package com.google.maps.data

import com.google.maps.data.models.FeaturesResponse
import retrofit2.Response
import retrofit2.http.GET

interface FeaturesApi {

    @GET("/.json")
    suspend fun getFeatures(): Response<FeaturesResponse>
}