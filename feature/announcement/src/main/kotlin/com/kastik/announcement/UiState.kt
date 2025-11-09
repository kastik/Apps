package com.kastik.announcement

import com.kastik.model.aboard.Announcement

sealed class UiState() {
    data class Success(val announcement: Announcement) : UiState()
    object Loading : UiState()
    object Error : UiState()

}