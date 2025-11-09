package com.kastik.network.api


import com.kastik.network.model.aboard.AboardAuthTokenDto
import com.kastik.network.model.aboard.AnnouncementResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AboardApiClient {
    @GET("announcements")
    suspend fun getAnnouncements(
        @Query("sortId") sortId: Int = 1,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): AnnouncementResponse


    @GET("authenticate")
    suspend fun exchangeCodeForAboardToken(
        @Query("code") code: String,
    ): AboardAuthTokenDto

}