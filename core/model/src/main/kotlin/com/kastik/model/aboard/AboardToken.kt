package com.kastik.model.aboard


data class AboardToken(
    val accessToken: String,
    val tokenType: String?,
    val userData: UserData,
    val expiresIn: Int,
)