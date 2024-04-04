package com.illeyrocci.centralcurrencies.data.local.database

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.illeyrocci.centralcurrencies.data.local.dao.CurrencyDao

//TODO(DATABASE ROOM)
internal abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        private var INSTANCE: CurrencyDatabase? = null
        private const val DB_NAME = "currencies.db"
        private val LOCK = Any()

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(LOCK) {
                INSTANCE
                    ?: buildDatabase(application).also { INSTANCE = it }
            }

        private fun buildDatabase(application: Application) =
            Room.databaseBuilder(
                application,
                CurrencyDatabase::class.java, DB_NAME
            )
                .build()
    }


}
