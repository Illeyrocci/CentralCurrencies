package com.illeyrocci.centralcurrencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource
import com.illeyrocci.centralcurrencies.domain.usecase.CacheCurrenciesUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.GetCurrenciesUseCase
import com.illeyrocci.centralcurrencies.domain.usecase.UploadCurrenciesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CurrencyViewModel(
    private val uploadCurrenciesUseCase: UploadCurrenciesUseCase,
    private val cacheCurrenciesUseCase: CacheCurrenciesUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {

    private var lastUpdateTime: String = "unknown"

    private val _currenciesStateStream =
        MutableStateFlow<Resource<List<CurrencyItem>>>(Resource.Loading())
    val currenciesStateStream: StateFlow<Resource<List<CurrencyItem>>>
        get() = _currenciesStateStream.asStateFlow()

    init {
        //TODO(getLastUpdated updateTime from preferences repository) and set [state]

        //TODO(set service or wormanager that will do getCurrencies()) and emit [currenciesState]
        refreshUiModel()
    }

    fun refreshUiModel() {
        viewModelScope.launch {
            _currenciesStateStream.value = Resource.Loading()
            val tryToUpload = uploadCurrenciesUseCase()
            _currenciesStateStream.value = if (tryToUpload is Resource.Error) {
                Resource.Error(tryToUpload.message ?: "", getCurrenciesUseCase().data)
            } else {
                tryToUpload.data?.let { cacheCurrenciesUseCase(tryToUpload.data) }
                getCurrenciesUseCase()
            }
        }
    }

    fun saveLastUpdateTime(time: String) {
        lastUpdateTime = time
    }
}
