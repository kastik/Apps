package com.kastik.apps.feature.settings

import com.kastik.apps.core.model.user.UserTheme

sealed class UiState {
    data object Loading : UiState()
    data class Success(
        val theme: UserTheme,
        val isDynamicColorEnabled: Boolean
    ) : UiState()
}