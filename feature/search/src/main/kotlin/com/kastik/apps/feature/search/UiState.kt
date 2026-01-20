package com.kastik.apps.feature.search

import com.kastik.apps.core.model.search.FilterOptions
import com.kastik.apps.core.model.search.QuickResults
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UiState(
    val activeFilters: ActiveFilters = ActiveFilters(),
    val availableFilters: FilterOptions = FilterOptions(),
    val quickResults: QuickResults = QuickResults(),
)

data class ActiveFilters(
    val activeQuery: String = "",
    val committedQuery: String = "",
    val selectedTagIds: ImmutableList<Int> = persistentListOf(),
    val selectedAuthorIds: ImmutableList<Int> = persistentListOf(),
)