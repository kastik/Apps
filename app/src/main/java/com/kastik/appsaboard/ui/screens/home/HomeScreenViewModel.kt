package com.kastik.appsaboard.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.appsaboard.domain.usecases.GetAnnouncementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getAnnouncementsUseCase: GetAnnouncementsUseCase,
) : ViewModel() {

    private val _uiState: MutableState<UiState> = mutableStateOf(UiState.Loading)
    val uiState: State<UiState> = _uiState

    init {
        loadAnnouncements()
    }



    fun loadAnnouncements() = viewModelScope.launch {
        runCatching {
            delay(500)
            _uiState.value = UiState.Success(data = getAnnouncementsUseCase())
        }.onFailure { e ->
            _uiState.value = UiState.Error(e.message ?: "Unknown error")
        }
    }
}