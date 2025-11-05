package com.kastik.appsaboard.domain.usecases

import com.kastik.appsaboard.domain.models.aboard.Announcement
import com.kastik.appsaboard.domain.repository.AnnouncementRepository

class GetAnnouncementsUseCase(
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke(): List<Announcement> =
        announcementRepository.getAnnouncements()
}