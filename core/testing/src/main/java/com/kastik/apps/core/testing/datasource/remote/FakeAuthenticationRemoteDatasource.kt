package com.kastik.apps.core.testing.datasource.remote

import com.kastik.apps.core.network.datasource.AuthenticationRemoteDataSource
import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto

class FakeAuthenticationRemoteDatasource : AuthenticationRemoteDataSource {

    var isTokenValid: Boolean = false
    var throwOnApiRequest: Throwable? = null
    var aboardAccessTokenResponse: AboardAuthTokenDto? = null

    override suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto {
        throwOnApiRequest?.let { throw it }
        aboardAccessTokenResponse?.let {
            return it
        }
        throw IllegalStateException("Aboard Access Token Response is null")
    }

    override suspend fun refreshAboardToken(token: String): AboardAuthTokenDto {
        throwOnApiRequest?.let { throw it }
        aboardAccessTokenResponse?.let {
            return it
        }
        throw IllegalStateException("Aboard Access Token Response is null")
    }

    override suspend fun checkIfTokenIsValid(): Boolean {
        throwOnApiRequest?.let { throw it }
        return isTokenValid
    }
}
