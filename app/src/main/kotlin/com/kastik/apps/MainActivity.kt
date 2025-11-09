package com.kastik.apps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.kastik.apps.ui.navigation.NavHost
import com.kastik.apps.ui.theme.AppsAboardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO Find a proper fix for this
        val field =
            Class.forName("android.database.CursorWindow").getDeclaredField("sCursorWindowSize")
        field.isAccessible = true
        field.set(null, 100 * 1024 * 1024) // 100MB
        enableEdgeToEdge()
        setContent {
            AppsAboardTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost()
                }

            }
        }
    }
}