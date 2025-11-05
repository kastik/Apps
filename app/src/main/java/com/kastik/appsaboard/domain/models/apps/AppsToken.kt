package com.kastik.appsaboard.domain.models.apps

data class AppsToken(
    val accessToken: String,
    val refreshToken: String?,
    val userId: String
)