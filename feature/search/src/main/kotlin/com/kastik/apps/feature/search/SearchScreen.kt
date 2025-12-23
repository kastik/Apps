package com.kastik.apps.feature.search

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kastik.apps.core.ui.SupplementaryContent
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.pagging.AnnouncementFeed
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.core.ui.placeholder.StatusContent
import com.kastik.apps.core.ui.sheet.GenericFilterSheet
import com.kastik.apps.core.ui.topbar.SearchBar
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreen(
    viewModel: SearchScreenViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()

    TrackScreenViewEvent("search_screen")

    SearchScreenContent(
        uiState = uiState.value,
        navigateBack = navigateBack,
        navigateToAnnouncement = navigateToAnnouncement,
        updateQuickResults = viewModel::updateQuickResults,
        updateSearchResults = viewModel::updateSearchResults,
        textFieldState = textFieldState,
        searchBarState = searchBarState
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContent(
    uiState: UiState,
    navigateToAnnouncement: (Int) -> Unit,
    updateQuickResults: (query: String) -> Unit,
    updateSearchResults: (query: String, tagsId: List<Int>, authorIds: List<Int>) -> Unit,
    textFieldState: TextFieldState,
    searchBarState: SearchBarState,

    navigateBack: () -> Unit,
) {
    val analytics = LocalAnalytics.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lazyResults = uiState.searchResults.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()
    val searchScroll = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    LocalFocusManager.current


    var showTagSheet by remember { mutableStateOf(false) }
    val tagSheetState = rememberModalBottomSheetState()
    var showAuthorSheet by remember { mutableStateOf(false) }
    val authorSheetState = rememberModalBottomSheetState()

    LaunchedEffect(textFieldState.text) {
        updateQuickResults(textFieldState.text.toString())
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            scrollBehavior = searchScroll,
            searchBarState = searchBarState,
            textFieldState = textFieldState,
            navigateToAnnouncement = navigateToAnnouncement,
            onSearch = updateSearchResults,
            tagsQuickResults = uiState.tagsQuickResults,
            authorsQuickResults = uiState.authorQuickResults,
            announcementsQuickResults = uiState.announcementQuickResults,
            actionButton = {},
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null
                    )
                }
            },
            expandedSupplementaryContent = {
                SupplementaryContent(
                    selectedTagsCount = uiState.selectedTagIds.size,
                    selectedAuthorsCount = uiState.selectedAuthorIds.size,
                    openTagSheet = {
                        showTagSheet = true
                    },
                    openAuthorSheet = {
                        showAuthorSheet = true
                    })
            },
            collapsedSupplementaryContent = {
                SupplementaryContent(
                    selectedTagsCount = uiState.selectedTagIds.size,
                    selectedAuthorsCount = uiState.selectedAuthorIds.size,
                    openTagSheet = {
                        scope.launch {
                            searchBarState.animateToExpanded()
                            showTagSheet = true
                        }
                    },
                    openAuthorSheet = {
                        scope.launch {
                            searchBarState.animateToExpanded()
                            showAuthorSheet = true
                        }
                    })
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            val refreshState = lazyResults.loadState.refresh
            val isEmpty by remember { derivedStateOf { lazyResults.itemCount == 0 } }

            AnnouncementFeed(
                onAnnouncementClick = { announcementId ->
                    analytics.logEvent(
                        "announcement_clicked", mapOf("announcement_id" to announcementId)
                    )
                    navigateToAnnouncement(announcementId)
                },
                onAnnouncementLongClick = { announcementId ->
                    shareAnnouncement(context, announcementId)
                },
                lazyAnnouncements = lazyResults,
                lazyListState = lazyListState,
                scrollBehavior = searchScroll,
            )

            if (refreshState is LoadState.Error && isEmpty) {
                StatusContent(
                    message = "Failed to load.",
                    action = { lazyResults.retry() },
                    actionText = "Retry",
                )
            }

            if (refreshState is LoadState.Loading && isEmpty) {
                LoadingContent("Fetching Announcements...", modifier = Modifier.fillMaxSize())
            }


        }
    }

    if (showTagSheet) {
        GenericFilterSheet(
            items = uiState.tags,
            selectedIds = uiState.selectedTagIds,
            onApply = { newTagIds ->
                analytics.logEvent(
                    "search_tags_updated", mapOf("tags" to newTagIds.toList())
                )
                updateSearchResults(uiState.query, newTagIds, uiState.selectedAuthorIds)
                scope.launch { tagSheetState.hide() }.invokeOnCompletion {
                    if (!tagSheetState.isVisible) {
                        showTagSheet = false
                    }
                }
            },
            sheetState = tagSheetState,
            onDismiss = {
                scope.launch { tagSheetState.hide() }.invokeOnCompletion {
                    if (!tagSheetState.isVisible) {
                        showTagSheet = false
                    }
                }
            },
            idProvider = { it.id },
            labelProvider = { it.title },
            titlePlaceholder = "Search Tags..."
        )
    }
    if (showAuthorSheet) {
        GenericFilterSheet(
            items = uiState.authors, // List<Author>
            selectedIds = uiState.selectedAuthorIds,
            onApply = { newAuthorIds ->
                analytics.logEvent(
                    "search_authors_updated", mapOf("authors" to newAuthorIds.toList())
                )
                updateSearchResults(uiState.query, uiState.selectedTagIds, newAuthorIds)
                scope.launch {
                    searchBarState.animateToCollapsed()
                    scope.launch { authorSheetState.hide() }.invokeOnCompletion {
                        if (!authorSheetState.isVisible) {
                            showAuthorSheet = false
                        }

                    }
                }
            },
            sheetState = authorSheetState,
            onDismiss = {
                scope.launch {
                    searchBarState.animateToCollapsed()
                    scope.launch { authorSheetState.hide() }.invokeOnCompletion {
                        if (!authorSheetState.isVisible) {
                            showAuthorSheet = false
                        }
                    }
                }
            },
            idProvider = { it.id },
            labelProvider = { "${it.name} [${it.announcementCount}]" },
            groupProvider = { it.name.first().uppercaseChar() }, // Turns on grouping
            titlePlaceholder = "Search Authors..."
        )
    }
}

//TODO This is copied/pasted across AnnouncementScreen/SearchScreen/HomeScreen, find a common module and hoist it
fun shareAnnouncement(
    context: Context, announcementId: Int
) {
    val url = "https://aboard.iee.ihu.gr/announcements/$announcementId"
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Check out this announcement!")
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Announcement via")
    context.startActivity(shareIntent)
}
