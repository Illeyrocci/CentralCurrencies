package com.illeyrocci.centralcurrencies.domain.usecase

import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository

class CacheCurrenciesUseCase(
    private val repo: CurrencyRepository
) {
    suspend operator fun invoke(list: List<CurrencyItem>) {
        repo.saveCurrenciesInDb(list)
    }
}