package com.kastik.apps.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FilterChip(
    label: String,
    selectedCount: Int,
    onClick: () -> Unit
) {
    ElevatedFilterChip(
        selected = selectedCount > 0,
        onClick = onClick,
        label = {
            Text(
                text = if (selectedCount > 0) "$label ($selectedCount)" else label,
                style = MaterialTheme.typography.titleSmall
            )
        },
        trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
    )
}