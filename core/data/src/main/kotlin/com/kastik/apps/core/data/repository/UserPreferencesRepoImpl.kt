package com.kastik.apps.core.data.repository

import com.kastik.apps.core.data.mappers.toDomain
import com.kastik.apps.core.data.mappers.toProto
import com.kastik.apps.core.datastore.UserPreferencesLocalDataSource
import com.kastik.apps.core.di.UserPrefsDatastore
import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.user.UserTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepoImpl @Inject constructor(
    @param:UserPrefsDatastore private val userPreferences: UserPreferencesLocalDataSource
) : UserPreferencesRepository {
    override suspend fun getHasSkippedSignIn(): Boolean {
        return userPreferences.getHasSkippedSignIn()
    }

    override suspend fun setHasSkippedSignIn(hasSkippedSignIn: Boolean) {
        return userPreferences.setHasSkippedSignIn(hasSkippedSignIn)
    }

    override fun getUserTheme(): Flow<UserTheme> {
        return userPreferences.getUserTheme().map { it.toDomain() }
    }

    override suspend fun setUserTheme(theme: UserTheme) {
        return userPreferences.setUserTheme(theme.toProto())
    }

    override fun getDynamicColor(): Flow<Boolean> {
        return userPreferences.getDynamicColor()
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        return userPreferences.setDynamicColor(enabled)
    }
}