package com.kastik.apps.feature.announcement

import android.accounts.AuthenticatorException
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kastik.apps.core.domain.usecases.DownloadAttachmentUseCase
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import com.kastik.apps.core.domain.usecases.RefreshAnnouncementWithIdUseCase
import com.kastik.apps.feature.announcement.navigation.AnnouncementRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class AnnouncementScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val downloadAttachmentUseCase: DownloadAttachmentUseCase,
    private val refreshAnnouncementWithIdUseCase: RefreshAnnouncementWithIdUseCase,
    private val getAnnouncementWithIdUseCase: GetAnnouncementWithIdUseCase,
) : ViewModel() {

    val args = savedStateHandle.toRoute<AnnouncementRoute>()
    private val errorState = MutableStateFlow<String?>(null)
    val uiState: StateFlow<UiState> = combine(
        getAnnouncementWithIdUseCase(args.id),
        errorState
    ) { announcement, errorMsg ->
        if (announcement != null) {
            val parts = parseHtmlWithImages(announcement.body).toImmutableList()
            UiState.Success(announcement, parts)
        } else if (errorMsg != null) {
            UiState.Error(errorMsg)
        } else {
            UiState.Loading
        }
    }.onStart {
        refreshAnnouncement()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )

    //TODO Clean this up
    fun refreshAnnouncement() {
        viewModelScope.launch {
            try {
                refreshAnnouncementWithIdUseCase(args.id)
                errorState.update { null }
            } catch (e: Exception) {
                val msg = when (e) {
                    is AuthenticatorException -> "Sign in required"
                    is UnknownHostException -> "No internet connection"
                    else -> "Something went wrong while refreshing."
                }
                errorState.update { msg }
            }
        }
    }

    fun downloadAttachment(
        announcementId: Int, attachmentId: Int, fileName: String, mimeType: String
    ) {
        viewModelScope.launch {
            downloadAttachmentUseCase(
                attachmentId = attachmentId,
                announcementId = announcementId,
                fileName = fileName,
                mimeType = mimeType
            )
        }
    }

    private suspend fun parseHtmlWithImages(html: String): List<ProcessedBody> =
        withContext(Dispatchers.Default) {
            val parts = mutableListOf<ProcessedBody>()
            val regex = """<img[^>]+src="([^">]+)"[^>]*>""".toRegex()
            var lastIndex = 0

            regex.findAll(html).forEach { match ->
                val start = match.range.first
                if (start > lastIndex) {
                    val textSegment = html.substring(lastIndex, start).trim()
                    if (textSegment.isNotEmpty()) {
                        parts.add(ProcessedBody.Text(AnnotatedString.fromHtml(textSegment)))
                    }
                }
                val src = match.groupValues[1]
                parts.add(ProcessedBody.Image(src))
                lastIndex = match.range.last + 1
            }

            if (lastIndex < html.length) {
                val remainingText = html.substring(lastIndex).trim()
                if (remainingText.isNotEmpty()) {
                    parts.add(ProcessedBody.Text(AnnotatedString.fromHtml(remainingText)))
                }
            }
            return@withContext parts
        }
}