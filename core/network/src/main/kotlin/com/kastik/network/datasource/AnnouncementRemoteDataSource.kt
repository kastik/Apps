package com.kastik.network.datasource

import com.kastik.di.AnnRetrofit
import com.kastik.network.api.AboardApiClient

class AnnouncementRemoteDataSource(
    @param:AnnRetrofit private val api: AboardApiClient
) {
    suspend fun fetchAnnouncements(page: Int, perPage: Int) =
        api.getAnnouncements(page = page, perPage = perPage)
}