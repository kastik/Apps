package com.kastik.apps.core.network.datasource

import com.kastik.apps.core.di.AnnRetrofit
import com.kastik.apps.core.network.api.AboardApiClient
import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto
import com.kastik.apps.core.network.model.aboard.Token

interface AuthenticationRemoteDataSource {
    suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto
    suspend fun refreshAboardToken(token: String): AboardAuthTokenDto
    suspend fun checkIfTokenIsValid(): Boolean
}


internal class AuthenticationRemoteDataSourceImpl(
    @AnnRetrofit private val aboardApiClient: AboardApiClient,
) : AuthenticationRemoteDataSource {

    override suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto =
        aboardApiClient.exchangeCodeForAboardToken(code = code)

    override suspend fun refreshAboardToken(token: String): AboardAuthTokenDto {
        return aboardApiClient.refreshToken(Token(token))
    }

    override suspend fun checkIfTokenIsValid(): Boolean {
        return runCatching {
            aboardApiClient.getUserInfo()
        }.isSuccess
    }

}