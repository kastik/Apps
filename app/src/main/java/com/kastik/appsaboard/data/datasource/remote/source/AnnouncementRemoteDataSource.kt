package com.kastik.appsaboard.data.datasource.remote.source

import com.kastik.appsaboard.data.datasource.remote.api.AboardApiClient
import com.kastik.appsaboard.data.datasource.remote.dto.aboard.AnnouncementDto

class AnnouncementRemoteDataSource(
    private val apiService: AboardApiClient
) {

    suspend fun fetchAnnouncements(): List<AnnouncementDto> =
        apiService.getAnnouncements().data

}