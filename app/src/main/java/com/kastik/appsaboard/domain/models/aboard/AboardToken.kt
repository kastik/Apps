package com.kastik.appsaboard.domain.models.aboard


data class AboardToken(
    val accessToken: String,
    val tokenType: String?,
    val userData: UserData,
    val expiresIn: Int,
)