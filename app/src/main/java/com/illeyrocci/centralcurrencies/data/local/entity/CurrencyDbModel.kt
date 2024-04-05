package com.illeyrocci.centralcurrencies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
internal data class CurrencyDbModel(
    @PrimaryKey val charCode: String,
    val name: String,
    val rate: Double
)