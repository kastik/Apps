package com.kastik.apps.feature.home

import com.kastik.apps.core.model.aboard.Announcement
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.model.aboard.Tag

data class UiState(
    val isSignedIn: Boolean = false,
    val showSignInNotice: Boolean = false,
    val tags: List<Tag> = emptyList(),
    val authors: List<Author> = emptyList(),
    val announcementQuickResults: List<Announcement> = emptyList(),
    val tagsQuickResults: List<Tag> = emptyList(),
    val authorQuickResults: List<Author> = emptyList(),
    val expandedSearchBar: Boolean = false,
)