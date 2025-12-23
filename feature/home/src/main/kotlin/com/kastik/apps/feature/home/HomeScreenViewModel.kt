package com.kastik.apps.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.domain.usecases.GetAnnouncementTagsUseCase
import com.kastik.apps.core.domain.usecases.GetAuthorQuickResultsUseCase
import com.kastik.apps.core.domain.usecases.GetAuthorsUseCase
import com.kastik.apps.core.domain.usecases.GetIsSignedInUseCase
import com.kastik.apps.core.domain.usecases.GetPagedAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetSearchQuickResultsAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetTagsQuickResults
import com.kastik.apps.core.domain.usecases.SetUserHasSkippedSignInUseCase
import com.kastik.apps.core.domain.usecases.ShowSignInNoticeRationalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val isSignedInUseCase: GetIsSignedInUseCase,
    private val getPagedAnnouncements: GetPagedAnnouncementsUseCase,
    private val setUserHasSkippedSignInUseCase: SetUserHasSkippedSignInUseCase,
    private val showSignInNoticeRationalUseCase: ShowSignInNoticeRationalUseCase,
    private val getAnnouncementTagsUseCase: GetAnnouncementTagsUseCase,
    private val getAuthorsUseCase: GetAuthorsUseCase,
    private val getTagsQuickResults: GetTagsQuickResults,
    private val getAuthorQuickResultsUseCase: GetAuthorQuickResultsUseCase,
    private val getSearchQuickResultsAnnouncementsUseCase: GetSearchQuickResultsAnnouncementsUseCase,
) : ViewModel() {


    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var quickResultsJob: Job? = null

    val homeFeedAnnouncements = _uiState.map { it.isSignedIn }.distinctUntilChanged()
        .flatMapLatest { _ -> getPagedAnnouncements() }.cachedIn(viewModelScope)


    init {
        getIsSignedIn()
        getShowSignInNoticeRational()
        getTags()
        getAuthors()
    }

    fun updateQuickResults(query: String) {
        quickResultsJob?.cancel()
        quickResultsJob = viewModelScope.launch {
            combine(
                getTagsQuickResults(query),
                getAuthorQuickResultsUseCase(query),
                getSearchQuickResultsAnnouncementsUseCase(query)
            ) { tags, authors, announcements ->
                _uiState.update {
                    it.copy(
                        tagsQuickResults = tags,
                        authorQuickResults = authors,
                        announcementQuickResults = announcements
                    )
                }
            }.collect()
        }
    }


    fun onSignInNoticeDismiss() {
        viewModelScope.launch {
            setUserHasSkippedSignInUseCase(true)
        }
    }

    fun getIsSignedIn() {
        viewModelScope.launch {
            isSignedInUseCase().distinctUntilChanged().catch {
                Log.e("HomeScreenViewModel", "Error getting isSignedIn", it)
            }.collect { isSignedIn ->
                _uiState.update { state ->
                    state.copy(
                        isSignedIn = isSignedIn
                    )
                }
            }
        }
    }

    fun getShowSignInNoticeRational() {
        viewModelScope.launch {
            showSignInNoticeRationalUseCase().distinctUntilChanged().catch {
                Log.e("HomeScreenViewModel", "Error getting showSignInNoticeRational", it)
            }.collect { showSignIn ->
                _uiState.update { state ->
                    state.copy(
                        showSignInNotice = showSignIn
                    )
                }
            }
        }
    }

    fun getAuthors() {
        viewModelScope.launch {
            try {
                getAuthorsUseCase().collect {
                    _uiState.value = _uiState.value.copy(
                        authors = it
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    authors = emptyList()
                )
            }

        }
    }


    fun getTags() {
        viewModelScope.launch {
            try {
                getAnnouncementTagsUseCase().collect {
                    _uiState.value = _uiState.value.copy(
                        tags = it
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    tags = emptyList()
                )
            }

        }
    }

}