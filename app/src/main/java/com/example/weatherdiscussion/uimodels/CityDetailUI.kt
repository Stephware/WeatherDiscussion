package com.example.weatherdiscussion.uimodels

data class CityDetailUI(
    val current: CityWeatherUI,
    val forecast: List<ForecastItemUI>,

)