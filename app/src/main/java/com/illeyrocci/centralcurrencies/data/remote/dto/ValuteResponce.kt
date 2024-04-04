package com.illeyrocci.centralcurrencies.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ValuteResponse(
    @SerializedName("Date")
    val date: String,
    @SerializedName("PreviousDate")
    val previousDate: String,
    @SerializedName("Timestamp")
    val timestamp: String,
    @SerializedName("Valute")
    val valute: Map<String, CurrencyDto>
)

