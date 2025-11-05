package com.kastik.appsaboard.domain.repository

import com.kastik.appsaboard.domain.models.aboard.Announcement

interface AnnouncementRepository {
    suspend fun getAnnouncements(): List<Announcement>
}