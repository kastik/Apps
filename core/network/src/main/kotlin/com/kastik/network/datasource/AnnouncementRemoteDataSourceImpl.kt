package com.kastik.network.datasource

import com.kastik.di.AnnRetrofit
import com.kastik.network.api.AboardApiClient
import com.kastik.network.model.aboard.AnnouncementResponse
import java.io.InputStream

interface AnnouncementRemoteDataSource {
    suspend fun fetchAnnouncements(page: Int, perPage: Int): AnnouncementResponse

class AnnouncementRemoteDataSource(
    @param:AnnRetrofit private val api: AboardApiClient
) : AnnouncementRemoteDataSource {
    override suspend fun fetchAnnouncements(page: Int, perPage: Int): AnnouncementResponse =
        api.getAnnouncements(page = page, perPage = perPage)
}