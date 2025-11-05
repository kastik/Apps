package com.kastik.appsaboard.data.repository

import com.kastik.appsaboard.data.datasource.remote.source.AnnouncementRemoteDataSource
import com.kastik.appsaboard.data.mappers.toAnnouncement
import com.kastik.appsaboard.domain.models.aboard.Announcement
import com.kastik.appsaboard.domain.repository.AnnouncementRepository

class AnnouncementRepoImpl(
    private val remoteDataSource: AnnouncementRemoteDataSource,
) : AnnouncementRepository {

    override suspend fun getAnnouncements(): List<Announcement> =
        remoteDataSource.fetchAnnouncements().map { it.toAnnouncement() }

}