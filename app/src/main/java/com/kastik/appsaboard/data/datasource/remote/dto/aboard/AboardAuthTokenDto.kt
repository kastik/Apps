package com.kastik.appsaboard.data.datasource.remote.dto.aboard

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AboardAuthTokenDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("user_data") val userData: UserDataDto,
    @SerialName("expires_in") val expiresIn: Int
)