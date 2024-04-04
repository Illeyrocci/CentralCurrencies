package com.illeyrocci.centralcurrencies.data.remote.dto

import com.google.gson.annotations.SerializedName

internal data class CurrencyDto(
    @SerializedName("ID")
    val id: String,
    @SerializedName("NumCode")
    val numCode: String,
    @SerializedName("CharCode")
    val charCode: String,
    @SerializedName("Nominal")
    val nominal: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Value")
    val value: Double,
    @SerializedName("Previous")
    val previous: Double
)

internal data class ValuteResponse(
    @SerializedName("Date")
    val date: String,
    @SerializedName("PreviousDate")
    val previousDate: String,
    @SerializedName("Timestamp")
    val timestamp: String,
    @SerializedName("Valute")
    val valute: Map<String, CurrencyDto>
)
