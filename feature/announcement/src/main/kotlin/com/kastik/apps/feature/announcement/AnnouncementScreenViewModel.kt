package com.kastik.apps.feature.announcement

import android.accounts.AuthenticatorException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.DownloadAttachmentUseCase
import com.kastik.apps.core.domain.usecases.GetAnnouncementWithIdUseCase
import com.kastik.apps.core.domain.usecases.RefreshAnnouncementWithIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@HiltViewModel(assistedFactory = AnnouncementScreenViewModel.Factory::class)
class AnnouncementScreenViewModel @AssistedInject constructor(
    @Assisted val announcementId: Int,
    private val downloadAttachmentUseCase: DownloadAttachmentUseCase,
    private val refreshAnnouncementWithIdUseCase: RefreshAnnouncementWithIdUseCase,
    private val getAnnouncementWithIdUseCase: GetAnnouncementWithIdUseCase,
) : ViewModel() {

    private val errorState = MutableStateFlow<String?>(null)
    val uiState: StateFlow<UiState> = combine(
        getAnnouncementWithIdUseCase(announcementId),
        errorState
    ) { announcement, errorMsg ->
        if (announcement != null) {
            UiState.Success(announcement)
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

    fun refreshAnnouncement() {
        viewModelScope.launch {
            try {
                refreshAnnouncementWithIdUseCase(announcementId)
                errorState.update { null }
            } catch (e: Exception) {
                val msg = when (e) {
                    is AuthenticatorException -> "Sign in required"
                    is UnknownHostException -> "No internet connection"
                    else -> "${e.message}"
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
                attachmentId, announcementId, fileName = fileName, mimeType = mimeType
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            announcementId: Int,
        ): AnnouncementScreenViewModel
    }
}