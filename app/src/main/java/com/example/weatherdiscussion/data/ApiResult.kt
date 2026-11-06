package com.example.weatherdiscussion.data

import com.bumptech.glide.load.engine.Initializable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okio.IOException
import retrofit2.HttpException
import kotlin.jvm.Throws
import kotlin.time.Duration.Companion.milliseconds

sealed interface ApiResult<out T>{
    data class Success<T>(val data: T): ApiResult<T>
    data class HttpError(val code: Int, val message: String?) : ApiResult<Nothing>
    data object NetworkError : ApiResult<Nothing>
    data class Unexpected(val cause: Throwable) : ApiResult<Nothing>
}

suspend fun <T> safeApiCall(json: Json, call: suspend() -> T) : ApiResult<T> =
    try {
        ApiResult.Success(call())
    } catch (e : CancellationException){
        throw e
    } catch(e: HttpException){
        ApiResult.HttpError(e.code(), e.message) // pwede na muna daw yan
    } catch(e: IOException){
        ApiResult.NetworkError
    } catch(e: SerializationException){
        ApiResult.Unexpected(e)
    }

suspend fun <T> retry(times: Int = 3, initialDelay: Long = 500, block: suspend() -> T): T {
    var delayMs = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            delay(delayMs.milliseconds)
            delayMs *= 2
        }
    }

    return block()
}