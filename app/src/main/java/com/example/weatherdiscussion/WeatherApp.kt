package com.example.weatherdiscussion

import android.app.Application
import com.example.weatherdiscussion.di.AppContainer

class WeatherApp : Application(){
    lateinit var container : AppContainer

    override fun onCreate(){
        super.onCreate()
        container = AppContainer(this)
    }
}