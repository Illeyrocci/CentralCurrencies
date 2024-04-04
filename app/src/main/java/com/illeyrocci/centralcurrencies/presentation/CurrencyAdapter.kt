package com.illeyrocci.centralcurrencies.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.illeyrocci.centralcurrencies.databinding.CurrencyViewItemBinding
import com.illeyrocci.centralcurrencies.domain.model.CurrencyItem

internal class CurrencyAdapter(
    private val context: Context
) : RecyclerView.Adapter<CurrencyViewHolder>() {

    private var data: List<CurrencyItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CurrencyViewItemBinding.inflate(inflater, parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun getItemCount(): Int =
        data.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, context)
    }

    fun submitData(currencyList: List<CurrencyItem>) {
        val diffCallback = CurrencyComparator(data, currencyList)
        val diffCurrencies = DiffUtil.calculateDiff(diffCallback)
        data = currencyList
        diffCurrencies.dispatchUpdatesTo(this)
    }
}