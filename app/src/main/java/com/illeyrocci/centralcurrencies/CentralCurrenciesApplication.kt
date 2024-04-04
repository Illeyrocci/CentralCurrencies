package com.illeyrocci.centralcurrencies

import android.app.Application
import com.illeyrocci.centralcurrencies.data.CurrencyRepositoryImpl
import com.illeyrocci.centralcurrencies.data.local.database.CurrencyDatabase
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository

class CentralCurrenciesApplication : Application() {

    //private lateinit var db: CurrencyDatabase
    //    private set

    override fun onCreate() {
        INSTANCE = this
        super.onCreate()
        //db = CurrencyDatabase.getInstance(this)
    }

    companion object {
        lateinit var INSTANCE: CentralCurrenciesApplication
            private set

        fun provideCurrencyRepository(): CurrencyRepository {
            return CurrencyRepositoryImpl.getInstance(INSTANCE)
        }
    }
}