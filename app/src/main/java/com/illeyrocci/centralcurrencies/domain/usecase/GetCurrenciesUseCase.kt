package com.illeyrocci.centralcurrencies.domain.usecase

import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository

class GetCurrenciesUseCase(
    private val repo: CurrencyRepository
) {
    suspend operator fun invoke(): Resource<List<CurrencyItem>> =
        repo.getCurrenciesFromDb()
}