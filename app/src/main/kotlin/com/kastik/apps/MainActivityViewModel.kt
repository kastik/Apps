package com.kastik.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kastik.apps.core.domain.usecases.GetDynamicColorUseCase
import com.kastik.apps.core.domain.usecases.GetUserThemeUseCase
import com.kastik.apps.core.model.user.UserTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getUserThemeUseCase: GetUserThemeUseCase, getDynamicColorUseCase: GetDynamicColorUseCase
) : ViewModel() {

    val themeState: StateFlow<ThemeState> = combine(
        getUserThemeUseCase(), getDynamicColorUseCase()
    ) { theme, dynamicColor ->
        ThemeState(theme, dynamicColor)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ThemeState(UserTheme.FOLLOW_SYSTEM, true)
    )

}

