package com.illeyrocci.centralcurrencies.data.remote

import com.illeyrocci.centralcurrencies.data.remote.dto.ValuteResponse
import retrofit2.http.GET

const val BASE_URL = "https://www.cbr-xml-daily.ru/"

interface NetworkAPI {

    @GET("daily_json.js")
    suspend fun getCurrencies(): ValuteResponse
}