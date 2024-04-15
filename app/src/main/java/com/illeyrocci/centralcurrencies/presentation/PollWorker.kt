package com.illeyrocci.centralcurrencies.presentation

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.illeyrocci.centralcurrencies.domain.repository.CurrencyRepository
import com.illeyrocci.centralcurrencies.domain.repository.PreferencesRepository
import com.illeyrocci.centralcurrencies.domain.usecase.EmitCurrenciesResourceUseCase

internal class PollWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        val getCurrenciesResourceUseCase = EmitCurrenciesResourceUseCase(
            CurrencyRepository.getInstance(),
            PreferencesRepository.getInstance()
        )
        getCurrenciesResourceUseCase()
        return Result.success()
    }
}