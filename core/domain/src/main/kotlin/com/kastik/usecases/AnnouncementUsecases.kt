package com.kastik.usecases

import androidx.paging.PagingData
import com.kastik.model.aboard.Announcement
import com.kastik.model.aboard.AnnouncementAuthor
import com.kastik.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow

class GetPagedAnnouncementsUseCase(
    private val repo: AnnouncementRepository
) {
    operator fun invoke(): Flow<PagingData<Announcement>> =
        repo.getPagedAnnouncements()
}

class GetAnnouncementWithIdUseCase(
    private val repo: AnnouncementRepository
) {
    suspend operator fun invoke(id: Int): Announcement =
        repo.getAnnouncementWithId(id)
}

class GetAuthorsUseCase(
    private val repo: AnnouncementRepository
) {
    operator fun invoke(): Flow<List<AnnouncementAuthor>> =
        repo.getAuthors()
}