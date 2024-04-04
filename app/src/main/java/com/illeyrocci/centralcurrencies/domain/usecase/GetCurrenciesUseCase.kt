package com.illeyrocci.centralcurrencies.domain.usecase

import com.illeyrocci.centralcurrencies.data.remote.dto.ValuteResponse
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository

class GetCurrenciesUseCase(
    private val repo: CurrencyRepository
) {
    suspend fun getCurrencies(): ValuteResponse {
        return repo.getCurrencies()
    }
}