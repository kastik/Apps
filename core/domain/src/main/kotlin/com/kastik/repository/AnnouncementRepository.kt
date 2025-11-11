package com.kastik.repository

import androidx.paging.PagingData
import com.kastik.model.aboard.AnnouncementAuthor
import com.kastik.model.aboard.AnnouncementPreview
import com.kastik.model.aboard.AnnouncementView
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    //TODO This breaks clean architecture find a way to abstract PagingData without a performance penalty
    fun getPagedAnnouncements(): Flow<PagingData<AnnouncementPreview>>

    suspend fun getAnnouncementWithId(id: Int): AnnouncementView

    fun getAuthors(): Flow<List<AnnouncementAuthor>>
}