package com.kastik.apps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kastik.apps.core.model.user.UserTheme
import com.kastik.apps.navigation.NavHost
import com.kastik.apps.ui.theme.AppsAboardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val uiState by viewModel.themeState.collectAsStateWithLifecycle()
            AppsAboardTheme(
                darkTheme = uiState.theme.shouldUseDarkTheme(),
                dynamicColor = uiState.dynamicColor
            ) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost()
                }
            }
        }
    }
}

data class ThemeState(
    val theme: UserTheme, val dynamicColor: Boolean
)

@Composable
fun UserTheme.shouldUseDarkTheme(): Boolean {
    return when (this) {
        UserTheme.DARK -> true
        UserTheme.LIGHT -> false
        UserTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    }
}