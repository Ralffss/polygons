package com.google.maps.main

import com.google.maps.data.FeaturesApi
import com.google.maps.data.models.FeaturesResponse
import com.google.maps.util.Resource
import javax.inject.Inject

class DefaultMapsRepository @Inject constructor(
    private val api: FeaturesApi
) : MapsRepository {

    override suspend fun getFeatures(): Resource<FeaturesResponse> {
        return try {
            val response = api.getFeatures()
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}