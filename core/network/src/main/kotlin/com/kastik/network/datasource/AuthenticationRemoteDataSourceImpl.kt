package com.kastik.network.datasource

import com.kastik.di.AnnRetrofit
import com.kastik.di.AuthRetrofit
import com.kastik.network.api.AboardApiClient
import com.kastik.network.api.AppsApiClient
import com.kastik.network.model.aboard.AboardAuthTokenDto
import com.kastik.network.model.aboard.UserProfileDto
import com.kastik.network.model.aboard.UserSubscribedTagDto
import com.kastik.network.model.apps.AppsAuthTokenDto

interface AuthenticationRemoteDataSource {
    suspend fun exchangeCodeForAppsToken(code: String): AppsAuthTokenDto

    suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto


    suspend fun checkIfTokenIsValid(): Boolean

    suspend fun getUserProfile(): UserProfileDto


    suspend fun getUserSubscriptions(): List<UserSubscribedTagDto>
}


class AuthenticationRemoteDataSourceImpl(
    @param:AuthRetrofit private val appsApiClient: AppsApiClient,
    @param:AnnRetrofit private val aboardApiClient: AboardApiClient,
    private val clientId: String = "690a9861468c9b767cabdc40",
    private val clientSecret: String = "5tf8ehb9tie8guqmpluj2unkydoj2bj4dtvfxq1jdj6cghsac3",
) : AuthenticationRemoteDataSource {
    override suspend fun exchangeCodeForAppsToken(code: String): AppsAuthTokenDto =
        appsApiClient.exchangeCodeForAppsToken(clientId, clientSecret, code = code)

    override suspend fun exchangeCodeForAboardToken(code: String): AboardAuthTokenDto =
        aboardApiClient.exchangeCodeForAboardToken(code = code)


    override suspend fun checkIfTokenIsValid(): Boolean {
        return runCatching {
            aboardApiClient.getUserInfo()
        }.isSuccess
    }

    override suspend fun getUserProfile(): UserProfileDto {
        return aboardApiClient.getUserInfo()
    }

    override suspend fun getUserSubscriptions(): List<UserSubscribedTagDto> {
        return aboardApiClient.getUserSubscriptions()
    }
}