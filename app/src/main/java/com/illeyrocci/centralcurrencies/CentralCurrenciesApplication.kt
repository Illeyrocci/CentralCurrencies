package com.illeyrocci.centralcurrencies

import android.app.Application
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository

class CentralCurrenciesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CurrencyRepository.initialize(this)
    }
}