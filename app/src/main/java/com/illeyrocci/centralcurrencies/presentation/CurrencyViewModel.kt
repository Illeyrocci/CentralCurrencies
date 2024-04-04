package com.illeyrocci.centralcurrencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import com.illeyrocci.centralcurrencies.domain.model.Resource
import com.illeyrocci.centralcurrencies.domain.usecase.GetCurrenciesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class CurrencyViewModel(
    private val getCurrencyUseCase: GetCurrenciesUseCase
) : ViewModel() {

    private var lastUpdateTime: String = "unknown"

    private val _currenciesStateStream = MutableStateFlow<Resource<List<CurrencyItem>>>(
        Resource.Error("Data not yet fetched")
    )
    val currenciesStateStream: StateFlow<Resource<List<CurrencyItem>>>
        get() = _currenciesStateStream.asStateFlow()

    init {
        //TODO(getLastUpdated updateTime from preferences repository) and set [state]
        //TODO(getCached data from database repository) and set [currenciesState]

        //TODO(set service or wormanager that will do getCurrencies()) and emit [currenciesState]
        getCurrencies()
    }

    private fun getCurrencies() = viewModelScope.launch {
        _currenciesStateStream.value = getCurrencyUseCase.getCurrencies()
    }

    fun setLastUpdatedTime(newTime: String) {
        lastUpdateTime = newTime
    }
}
