package com.illeyrocci.centralcurrencies.domain.usecase

import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import com.illeyrocci.centralcurrencies.domain.model.Resource

internal class GetCurrenciesUseCase(
    private val repo: CurrencyRepository
) {
    suspend fun getCurrencies(): Resource<List<CurrencyItem>> {
        return repo.getCurrencies()
    }
}