package com.example.weatherdiscussion.uimodels

data class CityWeatherUI(
    val city: City,
    val temperature: String,
    val feelsLike: String,
    val description: String,
    val humidity: String,
    val wind: String,
    val iconURL: String,
    val largeIconURL: String
)