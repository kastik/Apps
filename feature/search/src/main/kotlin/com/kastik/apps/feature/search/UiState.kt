package com.kastik.apps.feature.search

import androidx.paging.PagingData
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.coroutines.flow.Flow

data class UiState(
    val searchResults: Flow<PagingData<AnnouncementPreview>>,
    val query: String = "",
    val tags: List<Tag> = emptyList(),
    val selectedTagIds: List<Int> = emptyList(),
    val authors: List<Author> = emptyList(),
    val selectedAuthorIds: List<Int> = emptyList(),
    val showTagSheet: Boolean = false,
    val showAuthorSheet: Boolean = false,
    val announcementQuickResults: List<AnnouncementPreview> = emptyList(),
    val tagsQuickResults: List<Tag> = emptyList(),
    val authorQuickResults: List<Author> = emptyList()
)