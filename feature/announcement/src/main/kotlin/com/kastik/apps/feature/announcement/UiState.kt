package com.kastik.apps.feature.announcement

import com.kastik.apps.core.model.aboard.Announcement

sealed class UiState {
    data object Loading : UiState()
    data class Success(val announcement: Announcement) : UiState()
    data class Error(val message: String) : UiState()

}