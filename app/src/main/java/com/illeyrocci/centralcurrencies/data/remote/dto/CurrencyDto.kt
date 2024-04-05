package com.illeyrocci.centralcurrencies.data.remote.dto

import com.google.gson.annotations.SerializedName

internal data class CurrencyDto(
    @SerializedName("CharCode")
    val charCode: String,
    @SerializedName("Nominal")
    val nominal: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Value")
    val value: Double
)

internal data class ValuteResponse(
    @SerializedName("Valute")
    val valute: Map<String, CurrencyDto>
)
