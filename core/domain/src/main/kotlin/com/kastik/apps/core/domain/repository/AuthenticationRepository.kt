package com.kastik.apps.core.domain.repository

import kotlinx.coroutines.flow.Flow


interface AuthenticationRepository {
    fun getIsSignedIn(): Flow<Boolean>
    suspend fun refreshIsSignedIn()
    suspend fun refreshAboardToken()
    suspend fun exchangeCodeForAbroadToken(code: String)
    suspend fun getAboardToken(): String?
    suspend fun clearAuthenticationData()

}