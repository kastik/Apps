package com.kastik.repository

import androidx.paging.PagingData
import com.kastik.model.aboard.Announcement
import com.kastik.model.aboard.AnnouncementAuthor
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    //TODO This breaks clean architecture find a way to abstract PagingData without a performance penalty
    fun getPagedAnnouncements(): Flow<PagingData<Announcement>>

    suspend fun getAnnouncementWithId(id: Int): Announcement

    fun getAuthors(): Flow<List<AnnouncementAuthor>>
}