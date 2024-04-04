package com.illeyrocci.centralcurrencies.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illeyrocci.centralcurrencies.data.CurrencyRepositoryImpl
import com.illeyrocci.centralcurrencies.domain.usecase.GetCurrenciesUseCase

class CurrencyViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            return CurrencyViewModel(GetCurrenciesUseCase(CurrencyRepositoryImpl(application))) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}