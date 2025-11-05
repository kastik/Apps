package com.kastik.appsaboard.data.datasource.remote.source

import com.kastik.appsaboard.data.datasource.remote.api.AboardApiClient
import com.kastik.appsaboard.data.datasource.remote.api.AppsApiClient
import com.kastik.appsaboard.data.datasource.remote.dto.aboard.AboardAuthTokenDto
import com.kastik.appsaboard.data.datasource.remote.dto.apps.AppsAuthTokenDto

class AuthenticationRemoteDataSource(
    private val appsApiClient: AppsApiClient,
    private val aboardApiClient: AboardApiClient,
    private val clientId: String = "secret1",
    private val clientSecret: String = "secret2",
) {
    suspend fun exchangeCodeForAppsToken(code: String): AppsAuthTokenDto =
        appsApiClient.exchangeCodeForAppsToken(clientId, clientSecret, code = code)

    suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto =
        aboardApiClient.exchangeCodeForAboardToken(code = code)

}