package com.illeyrocci.centralcurrencies

import android.app.Application
import com.illeyrocci.centralcurrencies.data.local.database.CurrencyDatabase

class CentralCurrenciesApplication : Application() {

    private lateinit var db: CurrencyDatabase
    override fun onCreate() {
        INSTANCE = this
        super.onCreate()
        db = CurrencyDatabase.getInstance(this)

    }

    companion object {
        lateinit var INSTANCE: CentralCurrenciesApplication
            private set
    }
}