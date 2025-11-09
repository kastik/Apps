package com.kastik.announcement

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnnouncementScreen(
    viewModel: AnnouncementScreenViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    announcementId: Int,
) {
    val uiState = viewModel.uiState.value

    LaunchedEffect(Unit) {
        viewModel.getData(announcementId)
    }

    when (uiState) {
        UiState.Error -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Error")
            }

        }

        UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularWavyProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

        }

        is UiState.Success -> {


            Scaffold { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    Text(
                        text = uiState.announcement.title,
                        style = MaterialTheme.typography.titleLargeEmphasized,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = AnnotatedString.fromHtml(uiState.announcement.body),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Expressive tag cloud look
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.announcement.attachments.forEach { attachment ->
                            Log.d("MyLog", "attachment: $attachment")
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        attachment.filename,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            )
                        }
                    }

                    HorizontalDivider()

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.announcement.tags.forEach { tag ->
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        tag.title,
                                        style = MaterialTheme.typography.labelSmallEmphasized
                                    )
                                }
                            )
                        }
                    }
                    val options = listOf("Work", "Restaurant", "Coffee")
                    val unCheckedIcons =
                        listOf(
                            Icons.Outlined.Work,
                            Icons.Outlined.Restaurant,
                            Icons.Outlined.Coffee
                        )
                    val checkedIcons =
                        listOf(Icons.Filled.Work, Icons.Filled.Restaurant, Icons.Filled.Coffee)
                    var selectedIndex by remember { mutableIntStateOf(0) }
                    Row(
                        Modifier.padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
                    ) {
                        val modifiers =
                            listOf(Modifier.weight(1f), Modifier.weight(1.5f), Modifier.weight(1f))

                        options.forEachIndexed { index, label ->
                            ToggleButton(
                                checked = selectedIndex == index,
                                onCheckedChange = { selectedIndex = index },
                                modifier = modifiers[index].semantics { role = Role.RadioButton },
                                shapes =
                                    when (index) {
                                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                        options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                    },
                            ) {
                                Icon(
                                    if (selectedIndex == index) checkedIcons[index] else unCheckedIcons[index],
                                    contentDescription = "Localized description",
                                )
                                Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                                Text(label)
                            }
                        }
                    }
                }
            }
        }

    }

}

