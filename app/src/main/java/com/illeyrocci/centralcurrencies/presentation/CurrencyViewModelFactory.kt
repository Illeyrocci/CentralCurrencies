package com.illeyrocci.centralcurrencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illeyrocci.centralcurrencies.CentralCurrenciesApplication
import com.illeyrocci.centralcurrencies.domain.usecase.GetCurrenciesUseCase

internal class CurrencyViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            return CurrencyViewModel(GetCurrenciesUseCase(CentralCurrenciesApplication.provideCurrencyRepository())) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}