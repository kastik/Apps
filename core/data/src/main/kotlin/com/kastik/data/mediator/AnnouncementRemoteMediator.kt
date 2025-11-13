package com.kastik.data.mediator

import AnnouncementPreviewDatabaseView
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.kastik.data.mappers.toRoomEntities
import com.kastik.database.db.AppDatabase
import com.kastik.network.datasource.AnnouncementRemoteDataSource
import java.net.UnknownHostException

@OptIn(ExperimentalPagingApi::class)
class AnnouncementRemoteMediator(
    private val remote: AnnouncementRemoteDataSource,
    private val query: String? = null,
    private val db: AppDatabase
) : RemoteMediator<Int, AnnouncementPreviewDatabaseView>() {

    private val announcementDao = db.announcementDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    private var currentPage = 1
    private var lastPage = Int.MAX_VALUE

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, AnnouncementPreviewDatabaseView>
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


            if (loadType == LoadType.REFRESH) {
                db.clearAllTables()
            }
            response.data.forEach { dto ->
                announcementDao.addAnnouncement(
                    dto.toRoomEntities()
                )
            }
            currentPage = newCurrentPage
            lastPage = newLastPage
            return MediatorResult.Success(endOfPaginationReached = currentPage >= lastPage)

        } catch (e: UnknownHostException) {
            return MediatorResult.Error(e)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
