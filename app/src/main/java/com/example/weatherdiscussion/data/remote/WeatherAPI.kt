package com.example.weatherdiscussion.data.remote

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.weatherdiscussion.dto.CurrentWeatherDTO
import com.example.weatherdiscussion.dto.ForecastDTO
import com.example.weatherdiscussion.dto.GeoCityDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/weather")
    suspend fun currentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): CurrentWeatherDTO

    @GET("data/2.5/forecast")
    suspend fun forecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): ForecastDTO

    @GET("data/1.0/direct")
    suspend fun searchCities(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
    ): List<GeoCityDTO>

    companion object{
        const val BASE_URL = "https://api.openweathermap.org/"
        const val HOST = "api.openweathermap.org"
    }
}
