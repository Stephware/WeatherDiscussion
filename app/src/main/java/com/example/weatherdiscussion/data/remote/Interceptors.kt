package com.example.weatherdiscussion.data.remote

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class APIKeyInterceptor(private val apiKey: String, private val host: String = WeatherAPI.HOST) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.host != host) return chain.proceed(request)
        val url = request.url.newBuilder().addQueryParameter("appId", apiKey).build()
        return chain.proceed(request.newBuilder().url(url).build())
    }
}

// 10mins -> SAME RESPONSE 10MINS> -> SEND A NEW RESPONSE
class CacheControlInterceptor(private val maxAgeSeconds: Int = 600): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.proceed(chain.request())
        if (!request.isSuccessful) return request
        return request.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAgeSeconds")
            .removeHeader("Pragma")
            .build()
    }
}

class OfflineInterceptor(private val isOnline: () -> Boolean): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = if (isOnline()) chain.request() else
            chain.request().newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
        return chain.proceed(request)
    }
}