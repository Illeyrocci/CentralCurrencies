package com.illeyrocci.centralcurrencies.domain.usecase

import com.illeyrocci.centralcurrencies.domain.model.Resource
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import com.illeyrocci.centralcurrencies.domain.repository.PreferencesRepository
import java.text.DateFormat
import java.util.Date

class EmitCurrenciesResourceUseCase(
    private val currencyRepo: CurrencyRepository,
    private val preferencesRepo: PreferencesRepository
) {
    suspend operator fun invoke() {
        val tryToUpload = currencyRepo.getCurrenciesFromNetwork()
        currencyRepo.currenciesResourceFlow.value = if (tryToUpload is Resource.Error) {
            Resource.Error(tryToUpload.message ?: "", currencyRepo.getCurrenciesFromDb().data)
        } else {
            tryToUpload.data?.let { currencyRepo.saveCurrenciesInDb(tryToUpload.data) }
            preferencesRepo.setStoredTime(getCurrentTime())
            currencyRepo.getCurrenciesFromDb()
        }
    }

    private fun getCurrentTime() =
        DateFormat.getDateTimeInstance().format(Date().time)
}