package com.illeyrocci.centralcurrencies.domain.repository

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.illeyrocci.centralcurrencies.data.local.PreferencesRepositoryImpl
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException

interface PreferencesRepository {

    val storedTime: Flow<String>

    suspend fun setStoredTime(time: String)

    companion object {
        private var INSTANCE: PreferencesRepository? = null

        fun initialize(application: Application) {
            if (INSTANCE == null) {
                val dataStore = PreferenceDataStoreFactory.create {
                    application.preferencesDataStoreFile("settings")
                }

                INSTANCE = PreferencesRepositoryImpl(dataStore)
            }
        }

        fun getInstance(): PreferencesRepository {
            return INSTANCE ?: throw IllegalStateException(
                "PreferencesRepository must be initialized"
            )
        }
    }
}