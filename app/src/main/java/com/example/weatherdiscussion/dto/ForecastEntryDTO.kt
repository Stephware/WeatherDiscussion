package com.example.weatherdiscussion.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastEntryDTO(
    val dt: Long,
    val main: MainDTO,
    val weather: List<ConditionDTO> = emptyList(),
    @SerialName("dt_txt")
    val dateTimeText: String
) {
}