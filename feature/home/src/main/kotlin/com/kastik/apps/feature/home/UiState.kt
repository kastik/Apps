package com.kastik.apps.feature.home

import com.kastik.apps.core.model.search.FilterOptions
import com.kastik.apps.core.model.search.QuickResults

data class UiState(
    val isSignedIn: Boolean = false,
    val showSignInNotice: Boolean = false,
    val availableFilters: FilterOptions = FilterOptions(),
    val activeFilters: ActiveFilters = ActiveFilters(),
    val quickResults: QuickResults = QuickResults(),
)

data class ActiveFilters(
    val activeQuery: String = "",
)