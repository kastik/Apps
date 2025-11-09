package com.kastik.network.datasource

import com.kastik.network.api.AboardApiClient

class AnnouncementRemoteDataSource(
    private val api: AboardApiClient
) {
    suspend fun fetchAnnouncements(page: Int, perPage: Int) =
        api.getAnnouncements(page = page, perPage = perPage)
}