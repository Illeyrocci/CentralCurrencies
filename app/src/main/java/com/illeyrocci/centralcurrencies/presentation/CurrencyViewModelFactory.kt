package com.illeyrocci.centralcurrencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import com.illeyrocci.centralcurrencies.domain.usecase.CacheCurrenciesUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.GetCurrenciesUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.UploadCurrenciesUseCase

internal class CurrencyViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            val repo = CurrencyRepository.getInstance()
            return CurrencyViewModel(
                UploadCurrenciesUseCase(repo),
                CacheCurrenciesUseCase(repo),
                GetCurrenciesUseCase(repo)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}