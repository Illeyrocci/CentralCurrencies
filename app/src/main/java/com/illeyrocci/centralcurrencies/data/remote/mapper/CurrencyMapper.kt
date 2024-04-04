package com.illeyrocci.centralcurrencies.data.remote.mapper

import com.illeyrocci.centralcurrencies.data.remote.dto.CurrencyDto
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import kotlin.math.roundToInt

class CurrencyMapper {
    fun mapDtoListToModelList(dtoList: List<CurrencyDto>) =
        dtoList.map {
            CurrencyItem(
                charCode = it.charCode,
                name = it.name,
                //evaluate and crop Double at 5 digits after the decimal point
                rate = ((it.value / it.nominal) * 100000).roundToInt().toDouble() / 100000
            )
        }
}