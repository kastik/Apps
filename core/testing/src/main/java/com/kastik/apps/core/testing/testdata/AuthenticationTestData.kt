package com.kastik.apps.core.testing.testdata

import com.kastik.apps.core.network.model.aboard.AboardAuthTokenDto
import com.kastik.apps.core.network.model.aboard.UserDataDto

val aboardAuthTokenDtoTestData = AboardAuthTokenDto(
    accessToken = "Token",
    tokenType = "Bearer",
    userData = UserDataDto(id = 12),
    expiresIn = 6400,
)

