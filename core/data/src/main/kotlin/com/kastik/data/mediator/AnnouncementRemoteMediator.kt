package com.kastik.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kastik.data.mappers.toAttachmentEntities
import com.kastik.data.mappers.toAuthorEntity
import com.kastik.data.mappers.toEntity
import com.kastik.data.mappers.toTagCrossRefs
import com.kastik.data.mappers.toTagEntities
import com.kastik.database.db.AppDatabase
import com.kastik.database.entities.AnnouncementWithRelations
import com.kastik.network.datasource.AnnouncementRemoteDataSource

@OptIn(ExperimentalPagingApi::class)
class AnnouncementRemoteMediator(
    private val remote: AnnouncementRemoteDataSource,
    private val query: String? = null,
    private val db: AppDatabase
) : RemoteMediator<Int, AnnouncementWithRelations>() {

    private val announcementDao = db.announcementDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    private var currentPage = 1
    private var lastPage = Int.MAX_VALUE

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, AnnouncementWithRelations>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 1

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            LoadType.APPEND -> {
                if (currentPage >= lastPage) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                currentPage + 1
            }
        }

        try {
            val response = remote.fetchAnnouncements(page = page, perPage = state.config.pageSize)
            val newCurrentPage = response.meta.currentPage
            val newLastPage = response.meta.lastPage

            db.withTransaction {

                if (loadType == LoadType.REFRESH) {
                    announcementDao.clearTags()
                    announcementDao.clearAuthors()
                    announcementDao.clearAttachments()
                    announcementDao.clearTagCrossRefs()
                    announcementDao.clearAllAnnouncements()
                }

                response.data.forEach { dto ->
                    announcementDao.insertAnnouncement(dto.toEntity())
                    announcementDao.insertAuthor(dto.toAuthorEntity())
                    announcementDao.insertAttachments(dto.toAttachmentEntities())
                    announcementDao.insertTags(dto.toTagEntities())
                    announcementDao.insertTagCrossRefs(dto.toTagCrossRefs())
                }
            }
            currentPage = newCurrentPage
            lastPage = newLastPage
            return MediatorResult.Success(endOfPaginationReached = currentPage >= lastPage)

        } catch (e: Exception) {
            Log.d("MyLog", "Mediator error $e")
            return MediatorResult.Error(e)
        }
    }
}
