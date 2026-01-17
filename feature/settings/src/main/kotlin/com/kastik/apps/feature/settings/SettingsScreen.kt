package com.kastik.apps.feature.settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.kastik.apps.core.model.aboard.SortType
import com.kastik.apps.core.model.user.UserTheme
import com.kastik.apps.core.ui.extensions.LocalAnalytics
import com.kastik.apps.core.ui.extensions.TrackScreenViewEvent
import com.kastik.apps.core.ui.placeholder.LoadingContent

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun SettingsRoute(
    navigateToLicenses: () -> Unit,
    viewModel: SettingsScreenViewModel = hiltViewModel(),
) {
    TrackScreenViewEvent("settings_screen")

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = uiState, contentKey = { state ->
            state::class
        }) { state ->
        when (state) {

            is UiState.Loading -> LoadingContent(modifier = Modifier.fillMaxSize())

            is UiState.Success -> {
                SettingsScreenContent(
                    theme = state.theme,
                    setTheme = viewModel::setTheme,
                    sortType = state.sortType,
                    setSortType = viewModel::setSortType,
                    dynamicColorEnabled = state.isDynamicColorEnabled,
                    setDynamicColor = viewModel::setDynamicColor,
                    navigateToLicenses = navigateToLicenses
                )
            }

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun SettingsScreenContent(
    theme: UserTheme,
    setTheme: (UserTheme) -> Unit = {},
    sortType: SortType,
    setSortType: (SortType) -> Unit = {},
    dynamicColorEnabled: Boolean,
    setDynamicColor: (Boolean) -> Unit = {},
    navigateToLicenses: () -> Unit = {}
) {

    val context = LocalContext.current
    val analytics = LocalAnalytics.current
    val hapticFeedback = LocalHapticFeedback.current

    val areNotificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            Manifest.permission.POST_NOTIFICATIONS
        ).status.isGranted
    } else {
        true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings", style = MaterialTheme.typography.titleLarge
                    )
                })

        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(size = 20.dp)
                ) {
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Text("Sort by", style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(8.dp))
                            SortingSegmentedButton(
                                selected = sortType, onSelected = { sortType ->
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                    setSortType(sortType)
                                    analytics.logEvent(
                                        "sort_type_changed", mapOf("sort_type" to sortType.name)
                                    )
                                })
                        }
                    }
                }
            }

            item {
                Text("Appearance", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(size = 20.dp)
                ) {
                    Column {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Text("Theme", style = MaterialTheme.typography.bodyLarge)
                            Spacer(Modifier.height(8.dp))
                            ThemeSegmentedButton(
                                selected = theme, onSelected = { theme ->
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                    setTheme(theme)
                                    analytics.logEvent(
                                        "theme_changed", mapOf("theme" to theme.name)
                                    )
                                }

                            )
                        }
                        HorizontalDivider()
                        SettingSwitchRow(
                            title = "Dynamic color",
                            subtitle = "Use colors from the wallpaper",
                            checked = dynamicColorEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled) {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOn)
                                } else {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ToggleOff)
                                }
                                setDynamicColor(enabled)
                                analytics.logEvent(
                                    "dynamic_color_changed",
                                    mapOf("dynamic_color_enabled" to enabled.toString())
                                )
                            })
                    }
                }
            }

            item {
                Text("Notifications", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)
                ) {
                    Column {
                        SettingSwitchRow(
                            title = "Push notifications",
                            subtitle = "Receive updates and announcements",
                            checked = areNotificationGranted,
                            onCheckedChange = {
                                analytics.logEvent(
                                    "push_notifications_changed",
                                    mapOf("push_notifications_enabled" to it.toString())
                                )
                                val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                    }
                                } else {
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", context.packageName, null)
                                    }
                                }
                                context.startActivity(intent)
                            })
                        HorizontalDivider()
                        SettingSwitchRow(
                            title = "Email updates",
                            subtitle = "Send summaries to your inbox",
                            checked = false,
                            onCheckedChange = {
                                val text = "Not implemented yet!"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(context, text, duration) // in Activity
                                toast.show()

                            })
                    }
                }
            }

            item {
                Text("About", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)
                ) {
                    Column {
                        SettingNavigationRow(
                            title = "About app", subtitle = "Version 1.0", onClick = {
                                val text = "Not implemented yet!"
                                val duration = Toast.LENGTH_SHORT
                                val toast = Toast.makeText(context, text, duration) // in Activity
                                toast.show()

                            })
                        HorizontalDivider()
                        SettingNavigationRow(
                            title = "Open source licenses", onClick = {
                                analytics.logEvent("open_source_licenses_clicked")
                                navigateToLicenses()
                            })
                    }
                }
            }
        }
    }

}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked, onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingNavigationRow(
    title: String, subtitle: String? = null, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSegmentedButton(
    selected: UserTheme, onSelected: (UserTheme) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        SegmentedButton(
            selected = selected == UserTheme.FOLLOW_SYSTEM,
            onClick = { onSelected(UserTheme.FOLLOW_SYSTEM) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
        ) {
            Text("System")
        }

        SegmentedButton(
            selected = selected == UserTheme.LIGHT,
            onClick = { onSelected(UserTheme.LIGHT) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
        ) {
            Text("Light")
        }

        SegmentedButton(
            selected = selected == UserTheme.DARK,
            onClick = { onSelected(UserTheme.DARK) },
            shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
        ) {
            Text("Dark")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortingSegmentedButton(
    selected: SortType, onSelected: (SortType) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        SegmentedButton(
            selected = selected == SortType.Priority,
            onClick = { onSelected(SortType.Priority) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
        ) {
            Text("Priority")
        }

        SegmentedButton(
            selected = selected == SortType.DESC,
            onClick = { onSelected(SortType.DESC) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
        ) {
            Text("Descending")
        }
        SegmentedButton(
            selected = selected == SortType.ASC,
            onClick = { onSelected(SortType.ASC) },
            shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
        ) {
            Text("Ascending")
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        theme = UserTheme.FOLLOW_SYSTEM,
        setTheme = {},
        dynamicColorEnabled = true,
        setDynamicColor = {},
        sortType = SortType.Priority,
        setSortType = {},
    )
}
