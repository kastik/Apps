package com.kastik.data.repository

import com.kastik.datastore.AuthenticationLocalDataSource
import com.kastik.model.aboard.AboardToken
import com.kastik.model.aboard.UserData
import com.kastik.model.apps.AppsToken
import com.kastik.network.datasource.AuthenticationRemoteDataSource
import com.kastik.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val local: AuthenticationLocalDataSource,
    private val remote: AuthenticationRemoteDataSource,
) : AuthenticationRepository {

    override suspend fun exchangeCodeForAppsToken(code: String): AppsToken {
        val response = remote.exchangeCodeForAppsToken(code)
        local.saveAppsTokens(response.accessToken, response.refreshToken)
        return AppsToken(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            userId = response.userId
        )
    }

    override suspend fun exchangeCodeForAbroadToken(code: String): AboardToken {
        val response = remote.exchangeCodeForAboardToken(code)
        local.saveAboardToken((response.accessToken))
        return AboardToken(
            accessToken = response.accessToken,
            tokenType = response.tokenType,
            userData = UserData(response.userData.id),
            expiresIn = response.expiresIn,
        )
    }

    override suspend fun getSavedToken(): String? {
        return local.getAppsAccessToken()
    }
}