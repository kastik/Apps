package com.kastik.settings

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    Scaffold { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
        }
    }
}