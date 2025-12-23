package com.kastik.apps.core.ui.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.Author
import com.kastik.apps.core.model.aboard.Tag
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchBarState: SearchBarState,
    textFieldState: TextFieldState,
    scrollBehavior: SearchBarScrollBehavior,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actionButton: @Composable RowScope.() -> Unit = {},
    expandedSupplementaryContent: @Composable () -> Unit = {},
    collapsedSupplementaryContent: @Composable () -> Unit = {},
    navigateToAnnouncement: (Int) -> Unit,
    onSearch: (query: String, tagsId: List<Int>, authorIds: List<Int>) -> Unit,
    tagsQuickResults: List<Tag>,
    authorsQuickResults: List<Author>,
    announcementsQuickResults: List<AnnouncementPreview>,
) {


    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val searchBarInputField = remember {
        movableContentOf {
            SearchBarInputField(
                modifier = modifier,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                onSearch = {
                    onSearch(it, emptyList(), emptyList())
                },
            )

        }

    }

    SearchBarCollapsed(
        inputField = searchBarInputField,
        scrollBehavior = scrollBehavior,
        searchBarState = searchBarState,
        navigationIconButton = navigationIcon,
        actionIcons = actionButton,
        bottomContent = collapsedSupplementaryContent,
        modifier = modifier
    )

    SearchBarExpanded(
        modifier = modifier,
        onSearch = { query, tags, authors ->
            scope.launch {
                searchBarState.animateToCollapsed()
                onSearch(query, tags, authors)
            }
            focusManager.clearFocus()
        },
        navigateToAnnouncement = navigateToAnnouncement,
        searchBarState = searchBarState,
        inputField = searchBarInputField,
        announcements = announcementsQuickResults,
        tagsQuickResults = tagsQuickResults,
        authorsQuickResults = authorsQuickResults,
        supplementaryContent = expandedSupplementaryContent
    )
}