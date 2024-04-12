package com.illeyrocci.centralcurrencies.domain.usecase

import com.illeyrocci.centralcurrencies.domain.repository.PreferencesRepository

class SaveLastUpdateTimeUseCase(private val repository: PreferencesRepository) {
    suspend operator fun invoke(time: String) {
        repository.setStoredTime(time)
    }
}