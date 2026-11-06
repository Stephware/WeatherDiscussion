package com.example.weatherdiscussion.data

import com.example.weatherdiscussion.data.remote.WeatherAPI
import com.example.weatherdiscussion.dto.ConditionDTO
import com.example.weatherdiscussion.dto.CurrentWeatherDTO
import com.example.weatherdiscussion.dto.ForecastEntryDTO
import com.example.weatherdiscussion.uimodels.City
import com.example.weatherdiscussion.uimodels.CityDetailUI
import com.example.weatherdiscussion.uimodels.CityWeatherUI
import com.example.weatherdiscussion.uimodels.ForecastItemUI
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

interface WeatherRepository {
    suspend fun currentWeather(cities: List<City>) : ApiResult<List<CityWeatherUI>>
    suspend fun detail(city: City): ApiResult<CityDetailUI>
    suspend fun searchCities(query: String): ApiResult<List<City>>
}

class DefaultWeatherRepository(private val api: WeatherAPI, private val json: Json) : WeatherRepository{
    override suspend fun currentWeather(cities: List<City>) = safeApiCall(json){
        coroutineScope {
            cities.map { city ->
                async { retry { api.currentWeather(city.lat, city.lon)}.toUI(city) }
            }
        }.awaitAll()
    }

    override suspend fun detail(city: City) = safeApiCall(json) {
        coroutineScope {
            val current = async { retry { api.currentWeather(city.lat, city.lon) } }
            val forecast = async { retry { api.forecast(city.lat, city.lon)} }
            CityDetailUI(
                current = current.await().toUI(city),
                forecast = forecast.await().list.map{ it.toUI()}
            )
        }
    }

    override suspend fun searchCities(query: String) = safeApiCall(json){
        api.searchCities(query).map { geo ->
            City(
                name = listOfNotNull(geo.name, geo.state).joinToString(","),
                country = geo.country,
                lat = geo.lat,
                lon = geo.lon
            )
        }
    }
}

// EXTENSION FUNCTIONS
private fun Double.celsius() = "${roundToInt()}C"

fun CurrentWeatherDTO.toUI(city: City): CityWeatherUI {
    val condition = weather.firstOrNull()

    return CityWeatherUI(
        city = city,
        temperature = main.temp.celsius(),
        feelsLike = main.feelsLike.celsius(),
        description = condition?.description.orEmpty().capitalize(),
        humidity = "${main.humidity}%",
        wind = "${wind.speed} m/s",
        iconURL = iconURL(condition, "2x"),
        largeIconURL = iconURL(condition, "4x")
    )
}

private fun iconURL(condition: ConditionDTO?, size: String) : String{
    return condition?.let {"https://openweathermap.org/img/wn/${it.icon}@$size.png"}.orEmpty()
}

fun ForecastEntryDTO.toUI(): ForecastItemUI {
    val condition = weather.firstOrNull()

    return ForecastItemUI(
        id = dt,
        time = dateTimeText.substring(5, 16) + "UTC",
        temperature = main.temp.celsius(),
        description = condition?.description.orEmpty().capitalize(),
        iconURL = iconURL(condition, "2x")
    )
}

