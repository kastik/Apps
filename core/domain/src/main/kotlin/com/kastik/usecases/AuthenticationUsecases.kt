package com.kastik.usecases

import com.kastik.repository.AuthenticationRepository

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
