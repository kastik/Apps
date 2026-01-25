package com.kastik.apps.core.ui.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.kastik.apps.core.model.search.QuickResults
import com.kastik.apps.core.ui.announcement.AnnouncementCard
import kotlinx.collections.immutable.toImmutableList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarExpanded(
    quickResults: QuickResults,
    searchBarState: SearchBarState,
    inputField: @Composable () -> Unit,
    expandedSecondaryActions: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onTagQuickResultClick: (tagId: Int) -> Unit = {},
    onAuthorQuickResultClick: (authorId: Int) -> Unit = {},
    onAnnouncementQuickResultClick: (announcementId: Int) -> Unit = {},
) {
    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                expandedSecondaryActions()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { contentDescription = "search_bar:quick_results" }
                ) {
                    item {
                        SearchBarQuickResults(
                            modifier = Modifier.semantics {
                                contentDescription = "search_bar:tag_quick_results"
                            },
                            items = quickResults.tags,
                            icon = Icons.Default.Tag,
                            onItemClick = { tag ->
                                onTagQuickResultClick(tag.id)
                            },
                            labelProvider = { it.title },
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            thickness = 0.dp
                        )
                        SearchBarQuickResults(
                            modifier = Modifier.semantics {
                                contentDescription = "search_bar:author_quick_results"
                            },
                            items = quickResults.authors,
                            icon = Icons.Default.Person,
                            onItemClick = { author ->
                                onAuthorQuickResultClick(author.id)
                            },
                            labelProvider = { it.name },
                        )
                    }
                    if (quickResults.announcements.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 8.dp,
                                    bottom = 8.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Quick results",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                        }
                        items(quickResults.announcements) { item ->
                            AnnouncementCard(
                                modifier = Modifier.semantics {
                                    contentDescription = "search_bar:announcement_quick_results"
                                },
                                onClick = { onAnnouncementQuickResultClick(item.id) },
                                publisher = item.author,
                                title = item.title,
                                categories = remember(item.tags) {
                                    item.tags.map { it.title }.toImmutableList()
                                },
                                date = item.date,
                                content = remember(item.preview) { item.preview.orEmpty() },
                                isPinned = item.pinned
                            )
                        }
                    }
                }
            }
        }
    }
}

