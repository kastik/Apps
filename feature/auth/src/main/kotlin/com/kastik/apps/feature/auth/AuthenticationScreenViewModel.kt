package com.kastik.apps.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.ExchangeCodeForAboardTokenUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.RefreshUserProfileUseCase
import com.kastik.apps.core.domain.usecases.SubscribeToTagsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = AuthenticationScreenViewModel.Factory::class)
class AuthenticationScreenViewModel @AssistedInject constructor(
    @Assisted("code") code: String?,
    @Assisted("state") state: String?,
    @Assisted("error") error: String?,
    @Assisted("errorDesc") errorDesc: String?,
    private val exchangeCodeForToken: ExchangeCodeForAboardTokenUseCase,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val refreshSubscriptionsUseCase: RefreshSubscriptionsUseCase,
    private val subscribeToTagsUseCase: SubscribeToTagsUseCase,
) : ViewModel() {

    val uiState: StateFlow<UiState> = flow {
        if (!error.isNullOrBlank()) {
            emit(UiState.Error(error))
            return@flow
        }

        if (code.isNullOrBlank()) {
            emit(UiState.Error("Something went wrong"))
            return@flow
        }

        exchangeCodeForToken(code)
        refreshUserProfileUseCase()
        refreshSubscriptionsUseCase()
        emit(UiState.Success)

    }.catch {
        emit(UiState.Error("Something went wrong"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = UiState.Loading
    )

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("code") code: String?,
            @Assisted("state") state: String?,
            @Assisted("error") error: String?,
            @Assisted("errorDesc") errorDesc: String?
        ): AuthenticationScreenViewModel
    }
}