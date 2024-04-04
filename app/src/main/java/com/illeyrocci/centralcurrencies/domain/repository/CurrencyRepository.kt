package com.illeyrocci.centralcurrencies.domain.repository

import com.illeyrocci.centralcurrencies.data.remote.dto.ValuteResponse

interface CurrencyRepository {
    suspend fun getCurrencies(): ValuteResponse
}