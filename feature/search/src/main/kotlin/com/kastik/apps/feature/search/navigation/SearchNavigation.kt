package com.kastik.apps.feature.search.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.kastik.apps.feature.search.SearchScreen
import com.kastik.apps.feature.search.SearchScreenViewModel
import kotlinx.serialization.Serializable

@Serializable
data class SearchRoute(
    val query: String,
    val tags: List<Int>,
    val authors: List<Int>,
)

fun NavController.navigateToSearch(
    navOptions: NavOptions = navOptions {
        launchSingleTop = true
    },
    query: String = "",
    tags: List<Int> = emptyList(),
    authors: List<Int> = emptyList(),
) = navigate(route = SearchRoute(query = query, tags = tags, authors = authors), navOptions)


fun NavGraphBuilder.searchScreen(
    navigateBack: () -> Unit,
    navigateToAnnouncement: (Int) -> Unit,
) {
    composable<SearchRoute>(
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(350)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(350)
            )
        },
        popEnterTransition = {
            fadeIn()
        },
        exitTransition = {
            fadeOut()
        }
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<SearchRoute>()
        SearchScreen(
            navigateBack = navigateBack,
            navigateToAnnouncement = navigateToAnnouncement,
            viewModel = hiltViewModel<SearchScreenViewModel, SearchScreenViewModel.Factory> { factory ->
                factory.create(
                    args.query,
                    args.tags,
                    args.authors,
                )
            }
        )
    }
}




