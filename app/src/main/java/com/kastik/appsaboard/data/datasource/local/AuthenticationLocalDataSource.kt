package com.kastik.appsaboard.data.datasource.local


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AuthenticationLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val APPS_ACCESS_TOKEN_KEY = stringPreferencesKey("apps_access_token")
        val APPS_REFRESH_TOKEN_KEY = stringPreferencesKey("apps_refresh_token")
        val ABOARD_ACCESS_TOKEN_KEY = stringPreferencesKey("aboard_refresh_token")
    }

    suspend fun saveAppsTokens(accessToken: String, refreshToken: String?) {
        Log.d("MyLog", "Saving tokens")
        dataStore.edit {
            it[APPS_ACCESS_TOKEN_KEY] = accessToken
            refreshToken?.let { token -> it[APPS_REFRESH_TOKEN_KEY] = token }
        }
    }

    suspend fun getAppsAccessToken(): String? =
        dataStore.data.map { it[APPS_ACCESS_TOKEN_KEY] }.first()

    suspend fun saveAboardToken(accessToken: String) {
        dataStore.edit {
            it[ABOARD_ACCESS_TOKEN_KEY] = accessToken
        }
    }

    suspend fun getAboardAccessToken(): String? =
        dataStore.data.map { it[ABOARD_ACCESS_TOKEN_KEY] }.first()
}
