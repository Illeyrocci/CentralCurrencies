package com.illeyrocci.centralcurrencies.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.illeyrocci.centralcurrencies.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) : PreferencesRepository {

    override val storedTime: Flow<String> = dataStore.data.map {
        it[UPDATE_TIME_KEY] ?: ""
    }.distinctUntilChanged()

    override suspend fun setStoredTime(time: String) {
        dataStore.edit {
            it[UPDATE_TIME_KEY] = time
        }
    }

    companion object {
        private val UPDATE_TIME_KEY = stringPreferencesKey("update time")
    }
}