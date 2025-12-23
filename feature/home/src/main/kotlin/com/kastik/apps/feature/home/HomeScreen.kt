package com.kastik.apps.feature.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.LoadingIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.kastik.apps.core.designsystem.component.IEEDialog
import com.kastik.apps.core.designsystem.component.IEEFloatingToolBar
import com.kastik.apps.core.designsystem.theme.AppsAboardTheme
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.Attachment
import com.kastik.apps.core.model.aboard.Tag
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.extensions.isScrollingUp
import com.kastik.apps.core.ui.pagging.AnnouncementFeed
import com.kastik.apps.core.ui.placeholder.LoadingContent
import com.kastik.apps.core.ui.placeholder.StatusContent
import com.kastik.apps.core.ui.sheet.GenericFilterSheet
import com.kastik.apps.core.ui.topbar.SearchBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class, ExperimentalPermissionsApi::class,
)
@Composable
internal fun HomeScreenRoute(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSearch: (query: String, tagsId: List<Int>, authorIds: List<Int>) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyAnnouncementPagingItems = viewModel.homeFeedAnnouncements.collectAsLazyPagingItems()
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()

    TrackScreenViewEvent("home_screen")

    LaunchedEffect(textFieldState.text) {
        viewModel.updateQuickResults(textFieldState.text.toString())
    }

    if (uiState.isSignedIn) {
        NotificationRationale()
    }

    HomeScreenContent(
        uiState = uiState,
        announcements = lazyAnnouncementPagingItems,
        navigateToAnnouncement = navigateToAnnouncement,
        navigateToSettings = navigateToSettings,
        navigateToProfile = navigateToProfile,
        onSignInNoticeDismissed = viewModel::onSignInNoticeDismiss,
        navigateToSearch = navigateToSearch,
        textFieldState = textFieldState,
        searchBarState = searchBarState

    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
private fun HomeScreenContent(
    uiState: UiState,
    searchBarState: SearchBarState,
    textFieldState: TextFieldState,
    announcements: LazyPagingItems<AnnouncementPreview>,
    navigateToSearch: (query: String, tagsId: List<Int>, authorIds: List<Int>) -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit,
    onSignInNoticeDismissed: () -> Unit,
) {


    val context = LocalContext.current
    LocalFocusManager.current
    val analytics = LocalAnalytics.current
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    val searchScroll = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    var showTagSheet by remember { mutableStateOf(false) }
    val tagSheetState = rememberModalBottomSheetState()
    var showAuthorSheet by remember { mutableStateOf(false) }
    val authorSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    AnimatedVisibility(uiState.showSignInNotice) {
        IEEDialog(
            icon = Icons.AutoMirrored.Default.Login,
            title = "Sign in",
            text = "Sign in to unlock all announcements. You are currently browsing with limited access.",
            confirmText = "Sign-in",
            onConfirm = { onSignIn(context) },
            dismissText = "Dismiss",
            onDismiss = onSignInNoticeDismissed
        )
    }

    LaunchedEffect(searchBarState.currentValue) {
        if (searchBarState.currentValue == SearchBarValue.Collapsed) {
            textFieldState.clearText()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            scrollBehavior = searchScroll,
            searchBarState = searchBarState,
            textFieldState = textFieldState,
            navigateToAnnouncement = navigateToAnnouncement,
            onSearch = navigateToSearch,
            tagsQuickResults = uiState.tagsQuickResults,
            authorsQuickResults = uiState.authorQuickResults,
            announcementsQuickResults = uiState.announcementQuickResults,
            actionButton = {
                IconButton(onClick = {
                    analytics.logEvent("settings_clicked")
                    navigateToSettings()
                }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        if (uiState.isSignedIn) {
                            analytics.logEvent("profile_clicked")
                            navigateToProfile()
                        } else {
                            analytics.logEvent("sign_in_clicked")
                            onSignIn(context)
                        }
                    }) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
        )

        Box {
            val refreshState = announcements.loadState.refresh
            val isEmpty by remember { derivedStateOf { announcements.itemCount == 0 } }

            PullToRefreshBox(
                isRefreshing = refreshState is LoadState.Loading,
                state = pullToRefreshState,
                onRefresh = { announcements.refresh() },
                indicator = {
                    LoadingIndicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        state = pullToRefreshState,
                        isRefreshing = refreshState is LoadState.Loading,
                    )
                }) {
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
                    lazyAnnouncements = announcements,
                    lazyListState = lazyListState,
                    scrollBehavior = searchScroll,
                )

                if (refreshState is LoadState.Error && isEmpty) {
                    StatusContent(
                        message = "Failed to load.",
                        action = { announcements.retry() },
                        actionText = "Retry",
                    )
                }

                if (refreshState is LoadState.Loading && isEmpty) {
                    LoadingContent("Fetching Announcements...", modifier = Modifier.fillMaxSize())
                }
            }

            IEEFloatingToolBar(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                expanded = (lazyListState.isScrollingUp()),
                expandedAction = {
                    analytics.logEvent("scroll_up_clicked")
                    scope.launch { lazyListState.animateScrollToItem(0) }
                },
                collapsedAction = {
                    analytics.logEvent("search_clicked")
                    navigateToSearch(
                        "", emptyList(), emptyList()
                    )
                },
                expandedIcon = { Icon(Icons.Filled.ArrowUpward, null) },
                collapsedIcon = { Icon(Icons.Filled.Search, null) },
            )


        }
    }
    if (showTagSheet) {
        GenericFilterSheet(
            items = uiState.tags,
            selectedIds = emptyList(),
            onApply = { newTagIds ->
                analytics.logEvent(
                    "search_tags_updated", mapOf("tags" to newTagIds.toList())
                )
                navigateToSearch("", newTagIds, emptyList())
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
            // Extract Primitives here:
            idProvider = { it.id },
            labelProvider = { it.title },
            titlePlaceholder = "Search Tags..."
        )
    }
    if (showAuthorSheet) {
        GenericFilterSheet(
            items = uiState.authors,
            selectedIds = emptyList(),
            onApply = { newAuthorIds ->
                analytics.logEvent(
                    "search_authors_updated", mapOf("authors" to newAuthorIds.toList())
                )
                navigateToSearch("", emptyList(), newAuthorIds)
                scope.launch {
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationRationale() {
    var showRationale by rememberSaveable { mutableStateOf(true) }
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        null
    }

    notificationPermissionState?.let {
        if (!notificationPermissionState.status.isGranted && showRationale) {
            if (notificationPermissionState.status.shouldShowRationale) {
                IEEDialog(
                    icon = Icons.Default.NotificationsActive,
                    title = "Stay updated",
                    text = "Turn on notifications to never miss an important announcement.",
                    confirmText = "Allow",
                    onConfirm = { notificationPermissionState.launchPermissionRequest() },
                    dismissText = "Dismiss",
                    onDismiss = { showRationale = false })
            } else {
                LaunchedEffect(Unit) {
                    delay(2000)
                    notificationPermissionState.launchPermissionRequest()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = false, showBackground = true)
@Composable
fun PreviewHomeScreenContent() {
    val pagedAnnouncements = flowOf(PagingData.from(FakeAnnouncements)).collectAsLazyPagingItems()
    AppsAboardTheme {
        Surface {
            HomeScreenContent(
                uiState = UiState(),
                announcements = pagedAnnouncements,
                navigateToAnnouncement = {},
                navigateToSettings = {},
                navigateToProfile = {},
                onSignInNoticeDismissed = {},
                navigateToSearch = { _, _, _ -> },
                searchBarState = rememberSearchBarState(),
                textFieldState = rememberTextFieldState()
            )
        }
    }
}

private fun onSignIn(context: Context) {
    val url =
        "https://login.it.teithe.gr/authorization?" + "client_id=690a9861468c9b767cabdc40" + "&response_type=code" + "&scope=announcements,profile" + "&redirect_uri=com.kastik.apps://auth"
    val intent = Intent(
        Intent.ACTION_VIEW, url.toUri()
    )
    context.startActivity(intent)
}

val FakeTags = listOf(
    Tag(
        id = 1, title = "Tag1",
    ), Tag(
        id = 2, title = "Tag2",
    )
)

val FakeAttachments = listOf(
    Attachment(
        id = 1,
        filename = "image.jpg",
        fileSize = 1000,
        mimeType = "TODO()",
    )
)

val FakeAnnouncements = listOf<AnnouncementPreview>(
    AnnouncementPreview(
        id = 1,
        title = "Announcement Title",
        preview = "The quick brow fox jumps over the lazy dog",
        author = "Kostas",
        tags = FakeTags,
        attachments = FakeAttachments,
        date = "10-12-2025 11:45",
        pinned = false
    )
)