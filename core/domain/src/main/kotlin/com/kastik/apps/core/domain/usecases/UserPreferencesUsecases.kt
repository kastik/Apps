package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.UserPreferencesRepository
import com.kastik.apps.core.model.user.UserTheme
import kotlinx.coroutines.flow.Flow

class CheckIfUserHasSkippedSignInUseCase(
    private val repo: UserPreferencesRepository
) {
    suspend operator fun invoke(): Boolean =
        repo.getHasSkippedSignIn()
}

class SetUserHasSkippedSignInUseCase(
    private val repo: UserPreferencesRepository
) {
    suspend operator fun invoke(hasSkippedSignIn: Boolean) =
        repo.setHasSkippedSignIn(hasSkippedSignIn)
}

class GetUserThemeUseCase(
    private val repo: UserPreferencesRepository
) {
    operator fun invoke(): Flow<UserTheme> =
        repo.getUserTheme()
}

class SetUserThemeUseCase(
    private val repo: UserPreferencesRepository
) {
    suspend operator fun invoke(theme: UserTheme) =
        repo.setUserTheme(theme)
}


class GetDynamicColorUseCase(
    private val repo: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> =
        repo.getDynamicColor()
}

class SetDynamicColorUseCase(
    private val repo: UserPreferencesRepository
) {
    suspend operator fun invoke(enabled: Boolean) =
        repo.setDynamicColor(enabled)
}