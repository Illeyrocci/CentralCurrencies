package com.illeyrocci.centralcurrencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import com.illeyrocci.centralcurrencies.domain.repository.PreferencesRepository
import com.illeyrocci.centralcurrencies.domain.usecase.GetCurrenciesResourceFlowUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.GetLastUpdateTimeUseCase

internal class CurrencyViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            return CurrencyViewModel(
                GetCurrenciesResourceFlowUseCase(CurrencyRepository.getInstance()),
                GetLastUpdateTimeUseCase(PreferencesRepository.getInstance())
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}