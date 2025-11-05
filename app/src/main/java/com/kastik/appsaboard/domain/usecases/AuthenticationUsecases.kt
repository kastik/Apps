package com.kastik.appsaboard.domain.usecases

import com.kastik.appsaboard.domain.repository.AuthenticationRepository

class ExchangeCodeForAppsTokenUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(code: String) = repository.exchangeCodeForAppsToken(code)
}

class ExchangeCodeForAboardTokenUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(code: String) = repository.exchangeCodeForAbroadToken(code)
}
