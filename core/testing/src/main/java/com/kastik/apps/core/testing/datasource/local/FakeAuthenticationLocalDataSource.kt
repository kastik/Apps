package com.kastik.apps.core.testing.datasource.local

import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAuthenticationLocalDataSource : AuthenticationLocalDataSource {

    private val _isSignedIn = MutableStateFlow(false)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn.asStateFlow()

    private val _aboardToken = MutableStateFlow<String?>(null)
    val aboardToken: StateFlow<String?> = _aboardToken.asStateFlow()

    private val _aboardTokenExpiration = MutableStateFlow<Int?>(null)
    val aboardTokenExpiration: StateFlow<Int?> = _aboardTokenExpiration.asStateFlow()

    private val _aboardTokenLastRefreshTime = MutableStateFlow<Int?>(null)
    val aboardTokenLastRefreshTime: StateFlow<Int?> = _aboardTokenLastRefreshTime.asStateFlow()

    override fun getIsSignedIn(): Flow<Boolean> {
        return isSignedIn
    }

    override fun getAboardAccessToken(): Flow<String?> = aboardToken

    override suspend fun setAboardAccessToken(accessToken: String) {
        _aboardToken.value = accessToken
    }

    override suspend fun setAboardTokenExpiration(expiration: Int) {
        _aboardTokenExpiration.value = expiration
    }

    override suspend fun setAboardTokenLastRefreshTime(lastRefreshTime: Int) {
        _aboardTokenLastRefreshTime.value = lastRefreshTime
    }

    override fun getAboardTokenExpiration(): Flow<Int?> {
        return aboardTokenExpiration
    }

    override fun getAboardTokenLastRefreshTime(): Flow<Int?> {
        return aboardTokenLastRefreshTime
    }

    override suspend fun setIsSignedIn(isSignedIn: Boolean) {
        _isSignedIn.value = isSignedIn
    }

    override suspend fun clearAuthenticationData() {
        _aboardToken.value = null
        _aboardTokenExpiration.value = null
        _aboardTokenLastRefreshTime.value = null
        _isSignedIn.value = false
    }

    fun emitToken(newToken: String?) {
        _aboardToken.value = newToken
    }

}
