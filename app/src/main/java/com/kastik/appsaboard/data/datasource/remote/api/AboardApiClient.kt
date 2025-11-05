package com.kastik.appsaboard.data.datasource.remote.api

import com.kastik.appsaboard.data.datasource.remote.dto.aboard.AboardAuthTokenDto
import com.kastik.appsaboard.data.datasource.remote.dto.aboard.AnnouncementResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AboardApiClient {
    @GET("announcements")
    suspend fun getAnnouncements(
    ): AnnouncementResponse

    @GET("authenticate")
    suspend fun exchangeCodeForAboardToken(
        @Query("code") code: String,
    ): AboardAuthTokenDto

}