package com.kastik.apps.feature.licenses

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer


@Composable
internal fun LicensesRoute(
) {
    hiltViewModel<LicensesScreenViewModel>()
    LicensesScreen()
}

@Composable
internal fun LicensesScreen(
) {
    val libraries by produceLibraries(R.raw.aboutlibraries)
    Scaffold { paddingValues ->
        LibrariesContainer(
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = "licences:licence_list" },
            libraries = libraries,
            contentPadding = paddingValues
        )
    }
}