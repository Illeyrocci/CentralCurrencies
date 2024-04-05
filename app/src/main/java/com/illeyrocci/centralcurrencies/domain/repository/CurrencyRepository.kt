package com.illeyrocci.centralcurrencies.domain.repository

import com.illeyrocci.centralcurrencies.CentralCurrenciesApplication
import com.illeyrocci.centralcurrencies.data.CurrencyRepositoryImpl
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource

interface CurrencyRepository {
    suspend fun getCurrenciesFromNetwork(): Resource<List<CurrencyItem>>

    suspend fun getCurrenciesFromDb(): Resource<List<CurrencyItem>>

    suspend fun saveCurrenciesInDb(list: List<CurrencyItem>)

    companion object {
        fun provideCurrencyRepository(): CurrencyRepository {
            return CurrencyRepositoryImpl(CentralCurrenciesApplication.INSTANCE)
        }
    }
}