package com.illeyrocci.centralcurrencies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illeyrocci.centralcurrencies.data.remote.dto.CurrencyDto
import com.illeyrocci.centralcurrencies.data.remote.dto.ValuteResponse
import com.illeyrocci.centralcurrencies.domain.usecase.GetCurrenciesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel(
    private val getCurrencyUseCase: GetCurrenciesUseCase
) : ViewModel() {

    val lastUpdateTime = String

    private val _currenciesStream = MutableStateFlow<ValuteResponse>(
        ValuteResponse("","","", mapOf("" to CurrencyDto("", "", "", 1, "", 1.0, 1.0)))
    )
    val currenciesStream: StateFlow<ValuteResponse>
        get() = _currenciesStream.asStateFlow()

    init {
        //TODO(getLastUpdated updateTime from preferences repository) and set [state]
        //TODO(getCached data from database repository) and set [currenciesState]

        //TODO(set service or wormanager that will do getCurrencies()) and emit [currenciesState]
        getCurrencies()
    }

    private fun getCurrencies() = viewModelScope.launch {
        _currenciesStream.value = getCurrencyUseCase.getCurrencies()
    }
}
