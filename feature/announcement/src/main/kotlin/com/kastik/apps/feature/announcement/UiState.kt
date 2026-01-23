package com.kastik.apps.feature.announcement

import androidx.compose.ui.text.AnnotatedString
import com.kastik.apps.core.model.aboard.Announcement
import kotlinx.collections.immutable.ImmutableList

sealed class UiState {
    data object Loading : UiState()
    data class Success(
        val announcement: Announcement,
        val processedBody: ImmutableList<ProcessedBody>
    ) : UiState()

    data class Error(val message: String) : UiState()

}

sealed class ProcessedBody {
    data class Text(val text: AnnotatedString) : ProcessedBody()
    data class Image(val url: String) : ProcessedBody()
}