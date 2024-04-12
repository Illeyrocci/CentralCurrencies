package com.illeyrocci.centralcurrencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource
import com.illeyrocci.centralcurrencies.domain.usecase.CacheCurrenciesUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.GetCurrenciesUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.GetLastUpdateTimeUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.SaveLastUpdateTimeUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.UploadCurrenciesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Date

internal class CurrencyViewModel(
    private val uploadCurrenciesUseCase: UploadCurrenciesUseCase,
    private val cacheCurrenciesUseCase: CacheCurrenciesUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getLastUpdateTimeUseCase: GetLastUpdateTimeUseCase,
    private val saveLastUpdateTimeUseCase: SaveLastUpdateTimeUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(CurrenciesUiState())
    val uiState: StateFlow<CurrenciesUiState>
        get() = _uiState.asStateFlow()

    init {
        //TODO(set service or wormanager that will do getCurrencies()) and emit [currenciesState]
        viewModelScope.launch(Dispatchers.IO) {
            getLastUpdateTimeUseCase().collectLatest {
                _uiState.update { oldState ->
                    oldState.copy(lastUpdateTime = it)
                }
            }
        }
        refreshUiState()
    }

    fun refreshUiState() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { oldState ->
                oldState.copy(currenciesResource = Resource.Loading())
            }
            val currenciesResource = getCurrenciesResource()
            _uiState.update { oldState ->
                oldState.copy(currenciesResource = currenciesResource)
            }
        }
    }

    private suspend fun getCurrenciesResource(): Resource<List<CurrencyItem>> {
        val tryToUpload = uploadCurrenciesUseCase()
        return if (tryToUpload is Resource.Error) {
            Resource.Error(tryToUpload.message ?: "", getCurrenciesUseCase().data)
        } else {
            tryToUpload.data?.let { cacheCurrenciesUseCase(tryToUpload.data) }
            saveLastUpdateTimeUseCase(getCurrentTime())
            getCurrenciesUseCase()
        }
    }

    private fun getCurrentTime() =
        DateFormat.getDateTimeInstance().format(Date().time)
}

internal data class CurrenciesUiState(
    val currenciesResource: Resource<List<CurrencyItem>> = Resource.Success(emptyList()),
    val lastUpdateTime: String = ""
)
