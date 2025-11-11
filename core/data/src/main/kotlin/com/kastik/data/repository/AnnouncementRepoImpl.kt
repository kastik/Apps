package com.kastik.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kastik.data.mappers.toDomain
import com.kastik.data.mediator.AnnouncementRemoteMediator
import com.kastik.database.db.AppDatabase
import com.kastik.model.aboard.AnnouncementAuthor
import com.kastik.model.aboard.AnnouncementPreview
import com.kastik.model.aboard.AnnouncementView
import com.kastik.network.datasource.AnnouncementRemoteDataSource
import com.kastik.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnnouncementRepoImpl(
    private val remoteDataSource: AnnouncementRemoteDataSource,
    private val db: AppDatabase
) : AnnouncementRepository {

    @OptIn(ExperimentalPagingApi::class)
    //TODO This breaks clean architecture
    override fun getPagedAnnouncements(): Flow<PagingData<AnnouncementPreview>> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 60,
                prefetchDistance = 10,
                enablePlaceholders = true
            ),
            remoteMediator = AnnouncementRemoteMediator(remote = remoteDataSource, db = db),
            pagingSourceFactory = { db.announcementDao().getPagingAnnouncementPreviews() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }

    override suspend fun getAnnouncementWithId(id: Int): AnnouncementView {
        return db.announcementDao().getAnnouncementWithId(id).toDomain()
    }

    override fun getAuthors(): Flow<List<AnnouncementAuthor>> {
        return db.announcementDao().getAuthors().map { list ->
            list.map { it.toDomain() }
        }
    }

}