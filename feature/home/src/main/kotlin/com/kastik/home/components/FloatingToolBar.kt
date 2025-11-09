package com.kastik.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FloatingToolBar(
    expanded: Boolean,
    onFabClick: () -> Unit,
) {
    HorizontalFloatingToolbar(
        //scrollBehavior = toolbarScroll,
        expanded = expanded,
        floatingActionButton = {
            FloatingToolbarDefaults.VibrantFloatingActionButton(
                onClick = onFabClick
            ) {
                Icon(Icons.Filled.Add, null)
            }
        },
        //colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(),
        content = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Person, contentDescription = null)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Edit, contentDescription = null)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Favorite, contentDescription = null)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Filled.MoreVert, contentDescription = null)
            }
        },
    )
}