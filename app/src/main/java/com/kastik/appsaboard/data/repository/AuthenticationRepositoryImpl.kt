package com.kastik.appsaboard.data.repository

import com.kastik.appsaboard.data.datasource.local.AuthenticationLocalDataSource
import com.kastik.appsaboard.data.datasource.remote.source.AuthenticationRemoteDataSource
import com.kastik.appsaboard.domain.models.aboard.AboardToken
import com.kastik.appsaboard.domain.models.aboard.UserData
import com.kastik.appsaboard.domain.models.apps.AppsToken
import com.kastik.appsaboard.domain.repository.AuthenticationRepository

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
