package com.illeyrocci.centralcurrencies.data.local.mapper

import com.illeyrocci.centralcurrencies.data.local.entity.CurrencyDbModel
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem

internal class CurrencyMapperLocal {
    fun mapCurrencyItemListToCurrencyDbModelList(itemList: List<CurrencyItem>) =
        itemList.map { it.mapCurrencyItemToCurrencyDbModel() }

    fun mapCurrencyDbModelListToCurrencyItemList(dbModelList: List<CurrencyDbModel>) =
        dbModelList.map { it.mapCurrencyItemToCurrencyDbModel() }

    private fun CurrencyItem.mapCurrencyItemToCurrencyDbModel() =
        CurrencyDbModel(charCode, name, rate)

    private fun CurrencyDbModel.mapCurrencyItemToCurrencyDbModel() =
        CurrencyItem(charCode, name, rate)
}