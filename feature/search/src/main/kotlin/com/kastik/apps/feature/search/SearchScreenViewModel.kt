package com.kastik.apps.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kastik.apps.core.domain.usecases.GetAnnouncementTagsUseCase
import com.kastik.apps.core.domain.usecases.GetAuthorQuickResultsUseCase
import com.kastik.apps.core.domain.usecases.GetAuthorsUseCase
import com.kastik.apps.core.domain.usecases.GetPagedFilteredAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetSearchQuickResultsAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetTagsQuickResults
import com.kastik.apps.core.domain.usecases.RefreshAnnouncementTagsUseCase
import com.kastik.apps.core.domain.usecases.RefreshAuthorsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SearchScreenViewModel.Factory::class)
class SearchScreenViewModel @AssistedInject constructor(
    @Assisted("query") private val query: String,
    @Assisted("tag_ids") private val selectedTagIds: List<Int>,
    @Assisted("author_ids") private val selectedAuthorIds: List<Int>,
    private val getFilteredAnnouncementsUseCase: GetPagedFilteredAnnouncementsUseCase,
    private val getAnnouncementTagsUseCase: GetAnnouncementTagsUseCase,
    private val getAuthorsUseCase: GetAuthorsUseCase,
    private val refreshAuthorsUseCase: RefreshAuthorsUseCase,
    private val refreshAnnouncementTagsUseCase: RefreshAnnouncementTagsUseCase,
    private val getTagsQuickResults: GetTagsQuickResults,
    private val getAuthorQuickResultsUseCase: GetAuthorQuickResultsUseCase,
    private val getSearchQuickResultsAnnouncementsUseCase: GetSearchQuickResultsAnnouncementsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UiState(
            searchResults = getFilteredAnnouncementsUseCase(
                query,
                selectedAuthorIds,
                selectedTagIds
            ).cachedIn(viewModelScope)
        )
    )
    val uiState: StateFlow<UiState> = _uiState

    private var quickResultsJob: Job? = null

    init {
        getTags()
        getAuthors()
        refreshData()
        updateQuery(query)
        updateSelectedTagIds(selectedTagIds)
        updateSelectedAuthorIdsIds(selectedAuthorIds)
    }

    fun updateQuickResults(
        query: String
    ) {
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

    fun toggleTagSheet() {
        _uiState.value = _uiState.value.copy(
            showTagSheet = !_uiState.value.showTagSheet
        )
    }

    fun toggleAuthorSheet() {
        _uiState.value = _uiState.value.copy(
            showAuthorSheet = !_uiState.value.showAuthorSheet
        )
    }

    fun refreshData() {
        viewModelScope.launch {
            async {
                runCatching { refreshAuthorsUseCase() }
                runCatching { refreshAnnouncementTagsUseCase() }
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

    fun updateQuery(newQuery: String) {
        _uiState.value = _uiState.value.copy(
            query = newQuery
        )
        updateSearchResults()
    }

    fun updateSelectedTagIds(newTags: List<Int>) {
        _uiState.value = _uiState.value.copy(
            selectedTagIds = newTags
        )
        updateSearchResults()

    }

    fun updateSelectedAuthorIdsIds(newAuthors: List<Int>) {
        _uiState.value = _uiState.value.copy(
            selectedAuthorIds = newAuthors
        )
        updateSearchResults()
    }

    fun updateSearchResults(
        query: String,
        tagIds: List<Int>,
        authorIds: List<Int>
    ) {
        _uiState.value = _uiState.value.copy(
            query = query,
            selectedTagIds = tagIds,
            selectedAuthorIds = authorIds

        )
        updateSearchResults()
    }

    private fun updateSearchResults() {
        viewModelScope.launch {
            if (false
            //(uiState.value.query.trim().isBlank()) && uiState.value.selectedAuthorIds.isEmpty() && uiState.value.selectedTagIds.isEmpty()
            ) {
                // _uiState.value = _uiState.value.copy(searchResults = null,)
            } else {
                _uiState.value = _uiState.value.copy(
                    searchResults = getFilteredAnnouncementsUseCase(
                        query = _uiState.value.query,
                        authorIds = _uiState.value.selectedAuthorIds.toList(),
                        tagIds = _uiState.value.selectedTagIds.toList()
                    ).cachedIn(
                        viewModelScope
                    )
                )
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("query") query: String,
            @Assisted("tag_ids") selectedTagIds: List<Int>,
            @Assisted("author_ids") selectedAuthorIds: List<Int>,
        ): SearchScreenViewModel
    }

}
