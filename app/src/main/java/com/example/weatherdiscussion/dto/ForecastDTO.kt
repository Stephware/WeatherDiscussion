package com.example.weatherdiscussion.dto

import kotlinx.serialization.Serializable

@Serializable
data class ForecastDTO(val list: List<ForecastEntryDTO>){

}