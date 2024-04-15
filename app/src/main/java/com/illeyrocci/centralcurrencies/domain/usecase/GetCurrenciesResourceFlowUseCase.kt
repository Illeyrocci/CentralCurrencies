package com.illeyrocci.centralcurrencies.domain.usecase

import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow

class GetCurrenciesResourceFlowUseCase(
    private val currencyRepo: CurrencyRepository
) {
    operator fun invoke(): Flow<Resource<List<CurrencyItem>>> =
        currencyRepo.currenciesResourceFlow.asStateFlow()
}