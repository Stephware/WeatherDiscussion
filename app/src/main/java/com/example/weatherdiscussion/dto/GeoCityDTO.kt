package com.example.weatherdiscussion.dto

import kotlinx.serialization.Serializable

@Serializable
class GeoCityDTO(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String = "",
    val state: String? = null
) {
}