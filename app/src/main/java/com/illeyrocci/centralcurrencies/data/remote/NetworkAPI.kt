package com.illeyrocci.centralcurrencies.data.remote

import com.illeyrocci.centralcurrencies.data.remote.dto.ValuteResponse
import retrofit2.Response
import retrofit2.http.GET

internal const val BASE_URL = "https://www.cbr-xml-daily.ru/"

internal interface NetworkAPI {

    @GET("daily_json.js")
    suspend fun getCurrencies(): Response<ValuteResponse>
}