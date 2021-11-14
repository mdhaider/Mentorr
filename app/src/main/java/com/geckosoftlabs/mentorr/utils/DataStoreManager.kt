package com.geckosoftlabs.mentorr.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.geckosoftlabs.mentorr.features.login.Phonebook
import kotlinx.coroutines.flow.map


const val USER_DATASTORE = "mentorr_datastore"

class DataStoreManager(val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(USER_DATASTORE)

    companion object {
        val LOGGED_IN_KEY = booleanPreferencesKey("IS_LOGGED_IN")
    }

    suspend fun saveLoggedInState(loggedInStatus: Boolean) {
        context.dataStore.edit {
            it[LOGGED_IN_KEY] = loggedInStatus
        }
    }

    fun getLoggedInState() = context.dataStore.data.map {
        it[LOGGED_IN_KEY] ?: false
    }
}


