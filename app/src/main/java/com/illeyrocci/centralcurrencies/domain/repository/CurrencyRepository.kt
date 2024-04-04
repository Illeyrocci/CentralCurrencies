package com.illeyrocci.centralcurrencies.domain.repository

import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource

interface CurrencyRepository {
    suspend fun getCurrencies(): Resource<List<CurrencyItem>>
}