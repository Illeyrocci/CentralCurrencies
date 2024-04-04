package com.illeyrocci.centralcurrencies.presentation

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.illeyrocci.centralcurrencies.databinding.CurrencyViewItemBinding
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem
import java.util.Locale

class CurrencyViewHolder(
    private val binding: CurrencyViewItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(currencyItem: CurrencyItem, context: Context) {
        binding.apply {
            currencyName.text =
                if (context.resources.configuration.getLocales().get(0) == Locale("ru", "RU")) {
                    currencyItem.name
                } else {
                    currencyItem.charCode
                }
            currencyValue.text = currencyItem.rate.toString()
        }
    }
}