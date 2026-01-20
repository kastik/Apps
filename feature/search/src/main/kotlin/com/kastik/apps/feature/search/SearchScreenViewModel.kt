package com.kastik.apps.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import com.kastik.apps.core.domain.usecases.GetFilterOptionsUseCase
import com.kastik.apps.core.domain.usecases.GetPagedFilteredAnnouncementsUseCase
import com.kastik.apps.core.domain.usecases.GetQuickResultsUseCase
import com.kastik.apps.core.domain.usecases.RefreshFilterOptionsUseCase
import com.kastik.apps.feature.search.navigation.SearchRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQuickResultsUseCase: GetQuickResultsUseCase,
    private val getFilterOptionsUseCase: GetFilterOptionsUseCase,
    private val refreshFilterOptionsUseCase: RefreshFilterOptionsUseCase,
    private val getFilteredAnnouncementsUseCase: GetPagedFilteredAnnouncementsUseCase,
) : ViewModel() {

    private val _availableFilters = getFilterOptionsUseCase().onStart {
        viewModelScope.launch {
            runCatching { refreshFilterOptionsUseCase() }
        }
    }

    private val args = savedStateHandle.toRoute<SearchRoute>()

    private val _activeFilters = MutableStateFlow(
        ActiveFilters(
            activeQuery = args.query,
            committedQuery = args.query,
            selectedTagIds = args.tags.toImmutableList(),
            selectedAuthorIds = args.authors.toImmutableList()
        )
    )
    private val _quickSearchResults =
        _activeFilters.map { it.activeQuery }.flatMapLatest { activeQuery ->
            getQuickResultsUseCase(activeQuery)
        }

    val searchFeedAnnouncements = _activeFilters.flatMapLatest { activeFilters ->
        getFilteredAnnouncementsUseCase(
            activeFilters.committedQuery,
            activeFilters.selectedAuthorIds,
            activeFilters.selectedTagIds
        ).cachedIn(
            viewModelScope
        )
    }

    val uiState = combine(
        _availableFilters,
        _activeFilters,
        _quickSearchResults
    ) { availableFilters, activeFilters, quickResults ->
        UiState(
            availableFilters = availableFilters,
            activeFilters = activeFilters,
            quickResults = quickResults
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = UiState()
    )


    fun updateActiveQuery(query: String) {
        _activeFilters.update { activeFilters ->
            activeFilters.copy(activeQuery = query)
        }
    }

    fun commitQuery(query: String) {
        _activeFilters.update { activeFilters ->
            activeFilters.copy(committedQuery = query)
        }
    }

    fun updateSelectedTagIds(tagIds: ImmutableList<Int>) {
        _activeFilters.update { activeFilters ->
            activeFilters.copy(selectedTagIds = tagIds)
        }
    }

    fun updateSelectedAuthorIds(authorIds: ImmutableList<Int>) {
        _activeFilters.update { activeFilters ->
            activeFilters.copy(selectedAuthorIds = authorIds)
        }
    }
}