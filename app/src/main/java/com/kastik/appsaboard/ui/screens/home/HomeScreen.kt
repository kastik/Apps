package com.kastik.appsaboard.ui.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kastik.appsaboard.data.mappers.toTitleList
import com.kastik.appsaboard.domain.models.aboard.Announcement
import com.kastik.appsaboard.ui.screens.home.components.AnnouncementCard

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.value

    AnimatedContent(
        targetState = uiState, transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }) { state ->
        when (state) {
            is UiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Fetching Announcements...")
                }
            }

            is UiState.Success -> {
                LazyColumn {
                    item {
                        Button(
                            modifier = Modifier.padding(100.dp), onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW, Uri.parse(
                                        "https://login.it.teithe.gr/authorization?" + "client_id=690a9861468c9b767cabdc40" + "&response_type=code" + "&scope=announcements,profile" + "&redirect_uri=com.kastik.apps://auth"
                                    )
                                )
                                context.startActivity(intent)
                            }) {
                            Text("Sign in")
                        }
                    }
                    items(state.data) {
                        Row {
                            AnnouncementCard(
                                onClick = {},
                                publisher = it.author.name,
                                title = it.title,
                                categories = it.tags.toTitleList(),
                                date = it.updatedAt,
                                content = it.preview.orEmpty()
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.message}")
                }
            }
        }
    }
}


sealed class UiState {
    data object Loading : UiState()
    data class Success(val data: List<Announcement>) : UiState()
    data class Error(val message: String) : UiState()
}