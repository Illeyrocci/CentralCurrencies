package com.illeyrocci.centralcurrencies.data.remote.mapper

import com.illeyrocci.centralcurrencies.data.remote.dto.ValuteResponse
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import kotlin.math.roundToInt

internal class CurrencyMapper {
    fun mapValuteResponseToCurrencyItemList(valuteResponse: ValuteResponse): List<CurrencyItem> {
        val dtoList = valuteResponse.valute.values.toList()
        return dtoList.map {
            CurrencyItem(
                charCode = it.charCode,
                name = it.name,
                //evaluate and crop Double at 5 digits after the decimal point
                rate = ((it.value / it.nominal) * 100000).roundToInt().toDouble() / 100000
            )
        }
    }
}