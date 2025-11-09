package com.kastik.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.usecases.GetAuthorsUseCase
import com.kastik.usecases.GetPagedAnnouncementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    getPagedAnnouncements: GetPagedAnnouncementsUseCase,
    getAuthorsUseCase: GetAuthorsUseCase
) : ViewModel() {

    private val _announcements = getPagedAnnouncements().cachedIn(viewModelScope)
    val announcements = _announcements


    val authors = getAuthorsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    //private val _uiState: MutableState<UiState> = mutableStateOf(UiState.Loading)
    //val uiState: State<UiState> = _uiState

    init {
        //loadAnnouncements()
    }


    fun loadAnnouncements() = viewModelScope.launch {
        runCatching {
            //delay(1000)
            //_uiState.value = UiState.Success(data = _announcements.cachedIn(viewModelScope))
        }.onFailure { e ->
            //_uiState.value = UiState.Error(e.message ?: "Unknown error")
        }
    }
}