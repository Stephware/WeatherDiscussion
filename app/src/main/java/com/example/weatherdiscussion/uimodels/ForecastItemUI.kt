package com.example.weatherdiscussion.uimodels

data class ForecastItemUI(
    val id: Long,
    val time: String,
    val temperature: String,
    val description: String,
    val iconURL: String
) {
}