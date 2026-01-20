package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.model.search.FilterOptions
import com.kastik.apps.core.model.search.QuickResults
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetQuickResultsUseCase @Inject constructor(
    private val getTagsQuickResults: GetTagsQuickResults,
    private val getAuthorQuickResults: GetAuthorQuickResultsUseCase,
    private val getAnnouncementsQuickResults: GetSearchQuickResultsAnnouncementsUseCase
) {
    operator fun invoke(query: String): Flow<QuickResults> {
        return combine(
            getTagsQuickResults(query),
            getAuthorQuickResults(query),
            getAnnouncementsQuickResults(query)
        ) { tags, authors, announcements ->
            QuickResults(
                tags = tags,
                authors = authors,
                announcements = announcements
            )
        }
    }
}

class GetFilterOptionsUseCase @Inject constructor(
    private val getAuthorsUseCase: GetAuthorsUseCase,
    private val getAnnouncementTagsUseCase: GetAnnouncementTagsUseCase,
) {
    operator fun invoke(): Flow<FilterOptions> {
        return combine(
            getAuthorsUseCase(),
            getAnnouncementTagsUseCase(),
        ) { authors, tags ->
            FilterOptions(
                tags = tags,
                authors = authors,
            )
        }
    }
}

class RefreshFilterOptionsUseCase @Inject constructor(
    private val refreshAuthorsUseCase: RefreshAuthorsUseCase,
    private val refreshAnnouncementTagsUseCase: RefreshAnnouncementTagsUseCase,
) {
    suspend operator fun invoke() = coroutineScope {
        launch {
            refreshAuthorsUseCase()
        }
        launch {
            refreshAnnouncementTagsUseCase()
        }
    }
}