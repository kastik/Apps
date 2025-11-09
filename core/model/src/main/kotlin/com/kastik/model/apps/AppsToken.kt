package com.kastik.model.apps

data class AppsToken(
    val accessToken: String,
    val refreshToken: String?,
    val userId: String
)