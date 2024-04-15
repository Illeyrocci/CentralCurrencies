package com.illeyrocci.centralcurrencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.illeyrocci.centralcurrencies.CentralCurrenciesApplication
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource
import com.illeyrocci.centralcurrencies.domain.usecase.GetCurrenciesResourceFlowUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.GetLastUpdateTimeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

internal class CurrencyViewModel(
    private val getCurrenciesResourceFlowUseCase: GetCurrenciesResourceFlowUseCase,
    private val getLastUpdateTimeUseCase: GetLastUpdateTimeUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(CurrenciesUiState())
    val uiState: StateFlow<CurrenciesUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getLastUpdateTimeUseCase().collectLatest {
                _uiState.update { oldState ->
                    oldState.copy(lastUpdateTime = it)
                }
            }
        }

        viewModelScope.launch {
            getCurrenciesResourceFlowUseCase().collectLatest {
                _uiState.update { oldState ->
                    oldState.copy(currenciesResource = Resource.Loading())
                }
                _uiState.update { oldState ->
                    oldState.copy(currenciesResource = it)
                }
            }
        }

        initWork()
    }

    private fun initWork() {
        viewModelScope.launch(Dispatchers.Default) {
            setupDataUpdatingWork(0)

            observeDataUpdatingWork()
        }
    }

    fun setupDataUpdatingWork(delayInSeconds: Long) {

        val oneTimeRequest =
            OneTimeWorkRequestBuilder<PollWorker>().run {
                setInitialDelay(
                    delayInSeconds,
                    TimeUnit.SECONDS
                )
                build()
            }

        WorkManager.getInstance(CentralCurrenciesApplication.INSTANCE)
            .enqueueUniqueWork(
                PollWorker::class.java.simpleName,
                ExistingWorkPolicy.REPLACE,
                oneTimeRequest
            )
    }

    private suspend fun observeDataUpdatingWork() {
        withContext(Dispatchers.Main) {
            WorkManager.getInstance(CentralCurrenciesApplication.INSTANCE)
                .getWorkInfosForUniqueWorkLiveData(PollWorker::class.java.simpleName)
                .observeForever {
                    if (it[0].state == WorkInfo.State.SUCCEEDED) {
                        viewModelScope.launch(Dispatchers.Default) {
                            setupDataUpdatingWork(30)
                        }
                    }
                }
        }
    }
}

internal data class CurrenciesUiState(
    val currenciesResource: Resource<List<CurrencyItem>> = Resource.Success(emptyList()),
    val lastUpdateTime: String = ""
)
