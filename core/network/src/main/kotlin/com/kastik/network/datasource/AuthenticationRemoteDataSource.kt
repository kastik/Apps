package com.kastik.network.datasource

import com.kastik.network.api.AboardApiClient
import com.kastik.network.api.AppsApiClient
import com.kastik.network.model.aboard.AboardAuthTokenDto
import com.kastik.network.model.apps.AppsAuthTokenDto


//TODO Removed this pre-commit
class AuthenticationRemoteDataSource(
    private val appsApiClient: AppsApiClient,
    private val aboardApiClient: AboardApiClient,
    private val clientId: String = "Secret 1",
    private val clientSecret: String = "Secret 2",
) {
    suspend fun exchangeCodeForAppsToken(code: String): AppsAuthTokenDto =
        appsApiClient.exchangeCodeForAppsToken(clientId, clientSecret, code = code)

    suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto =
        aboardApiClient.exchangeCodeForAboardToken(code = code)

}