package com.illeyrocci.centralcurrencies.data.local.database

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.illeyrocci.centralcurrencies.data.local.dao.CurrencyDao

//TODO(DATABASE ROOM)
abstract class CurrencyDatabase : RoomDatabase() {
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

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CurrencyDatabase::class.java, DB_NAME
            )
                .build()
    }


}
