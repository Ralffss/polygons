package com.google.maps.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.maps.data.models.FeaturesResponse
import com.google.maps.util.DispatcherProvider
import com.google.maps.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: MapsRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val featureLiveData = MutableLiveData<FeaturesResponse>()

    fun fetchFeatures() {
        viewModelScope.launch(dispatchers.io) {
            when (val featuresResponse = repository.getFeatures()) {
                is Resource.Error -> Log.e("Error", "An error occurred")
                is Resource.Success -> {
                    featureLiveData.postValue(featuresResponse.data!!)
                }
            }
        }
    }
}