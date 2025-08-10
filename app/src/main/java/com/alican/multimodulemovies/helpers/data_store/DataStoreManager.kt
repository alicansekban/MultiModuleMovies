package com.alican.multimodulemovies.helpers.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.algidaDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DataStoreKeys.PREFERENCES_NAME,
    corruptionHandler = ReplaceFileCorruptionHandler(
        // This will replace the corrupted file with empty preferences
        produceNewData = { emptyPreferences() }
    )
)

class DataStoreManager @Inject constructor(
    private val context: Context
) {

    private suspend fun <T> DataStore<Preferences>.getFromLocalStorage(
        preferencesKey: Preferences.Key<T>
    ): T? {
        return data.map {
            it[preferencesKey]
        }.firstOrNull()
    }

    suspend fun <T> readValue(key: Preferences.Key<T>): T? {
        return context.algidaDataStore.getFromLocalStorage(key)
    }

    suspend fun <T> storeValue(key: Preferences.Key<T>, value: T) {
        context.algidaDataStore.edit {
            it[key] = value
        }
    }

    suspend fun <T> removeValue(key: Preferences.Key<T>) {
        context.algidaDataStore.edit {
            it.remove(key)
        }
    }

    suspend fun <T : Preferences.Key<*>> removeValues(keyList: List<T>) {
        keyList.forEach {
            removeValue(it)
        }
    }

    suspend fun removeAll() {
        context.algidaDataStore.edit {
            it.clear()
        }
    }
}