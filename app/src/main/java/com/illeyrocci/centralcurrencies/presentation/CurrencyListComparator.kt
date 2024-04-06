package com.illeyrocci.centralcurrencies.presentation

import androidx.recyclerview.widget.DiffUtil
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem

internal class CurrencyListComparator(
    private val oldList: List<CurrencyItem>,
    private val newList: List<CurrencyItem>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getOldListSize() =
        oldList.size

    override fun getNewListSize() =
        newList.size
}