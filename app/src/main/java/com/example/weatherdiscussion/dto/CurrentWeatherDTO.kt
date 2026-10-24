package com.example.weatherdiscussion.dto

import kotlinx.serialization.Serializable

@Serializable
class CurrentWeatherDTO(
    val name: String,
    val main: MainDTO,
    val weather: List<ConditionDTO> = emptyList(),
    val wind: WindDTO
)
