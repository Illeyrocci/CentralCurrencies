package com.illeyrocci.centralcurrencies.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.illeyrocci.centralcurrencies.data.local.entity.CurrencyDbModel

@Dao
internal interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyDbModel>)

    @Query("SELECT * FROM currencies")
    suspend fun getCurrencies(): List<CurrencyDbModel>
}