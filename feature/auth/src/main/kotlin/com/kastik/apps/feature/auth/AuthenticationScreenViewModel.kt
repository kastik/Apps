package com.kastik.apps.feature.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kastik.apps.core.domain.usecases.ExchangeCodeForAboardTokenUseCase
import com.kastik.apps.core.domain.usecases.RefreshSubscriptionsUseCase
import com.kastik.apps.core.domain.usecases.RefreshUserProfileUseCase
import com.kastik.apps.core.domain.usecases.SignOutUserUseCase
import com.kastik.apps.core.domain.usecases.StartTokenRefreshJob
import com.kastik.apps.core.domain.usecases.SubscribeToTagsUseCase
import com.kastik.apps.feature.auth.navigation.AuthRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthenticationScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exchangeCodeForToken: ExchangeCodeForAboardTokenUseCase,
    private val refreshUserProfileUseCase: RefreshUserProfileUseCase,
    private val refreshSubscriptionsUseCase: RefreshSubscriptionsUseCase,
    private val subscribeToTagsUseCase: SubscribeToTagsUseCase,
    private val startTokenRefreshJob: StartTokenRefreshJob,
    private val signOutUserUseCase: SignOutUserUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<AuthRoute>()

    val uiState: StateFlow<UiState> = flow {
        if (!args.error.isNullOrBlank()) {
            emit(UiState.Error(args.error))
            return@flow
        }

        if (args.code.isNullOrBlank()) {
            emit(UiState.Error("Something went wrong"))
            return@flow
        }

        exchangeCodeForToken(args.code)
        refreshUserProfileUseCase()
        refreshSubscriptionsUseCase()
        startTokenRefreshJob()
        emit(UiState.Success)

    }.catch {
        signOutUserUseCase()
        emit(UiState.Error("Something went wrong"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = UiState.Loading
    )
}