package com.illeyrocci.centralcurrencies.domain.repository

import android.app.Application
import com.illeyrocci.centralcurrencies.data.CurrencyRepositoryImpl
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource

interface CurrencyRepository {
    suspend fun getCurrenciesFromNetwork(): Resource<List<CurrencyItem>>

    suspend fun getCurrenciesFromDb(): Resource<List<CurrencyItem>>

    suspend fun saveCurrenciesInDb(list: List<CurrencyItem>)

    companion object {
        private var INSTANCE: CurrencyRepositoryImpl? = null
        fun initialize(application: Application) {
            if (INSTANCE == null) {
                INSTANCE = CurrencyRepositoryImpl(application)
            }
        }

        fun getInstance(): CurrencyRepository {
            return INSTANCE ?: throw IllegalStateException(
                "CurrencyRepositoryImpl must be initialized"
            )
        }
    }
}