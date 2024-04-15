package com.illeyrocci.centralcurrencies

import android.app.Application
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import com.illeyrocci.centralcurrencies.domain.repository.PreferencesRepository

class CentralCurrenciesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        CurrencyRepository.initialize(this)
        PreferencesRepository.initialize(this)
    }

    companion object {

        lateinit var  INSTANCE: CentralCurrenciesApplication
            private set
    }
}