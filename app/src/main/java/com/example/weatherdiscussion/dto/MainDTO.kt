package com.example.weatherdiscussion.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainDTO (
    val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    val humidity: Int
) {
}