package com.kastik.appsaboard.domain.repository

import com.kastik.appsaboard.domain.models.aboard.AboardToken
import com.kastik.appsaboard.domain.models.apps.AppsToken

interface AuthenticationRepository {
    suspend fun exchangeCodeForAppsToken(code: String): AppsToken

    suspend fun exchangeCodeForAbroadToken(code: String): AboardToken

    suspend fun getSavedToken(): String?
}