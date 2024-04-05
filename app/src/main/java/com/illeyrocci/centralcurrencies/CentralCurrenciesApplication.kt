package com.illeyrocci.centralcurrencies

import android.app.Application

class CentralCurrenciesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: CentralCurrenciesApplication
            private set
    }
}