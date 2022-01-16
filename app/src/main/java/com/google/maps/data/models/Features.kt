package com.google.maps.data.models

data class FeaturesResponse(
    val features: List<Feature>
)

data class Feature(
    val id: Int,
    val points: List<Point>
)

data class Point(
    val accuracy: Double,
    val latitude: Double,
    val longitude: Double
)
