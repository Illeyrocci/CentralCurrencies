package com.illeyrocci.centralcurrencies.domain.usecase

import com.illeyrocci.centralcurrencies.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetLastUpdateTimeUseCase(
    private val preferencesRepo: PreferencesRepository
) {
    operator fun invoke(): Flow<String> =
        preferencesRepo.storedTime
}