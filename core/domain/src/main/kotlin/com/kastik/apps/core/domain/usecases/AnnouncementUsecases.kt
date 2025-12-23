package com.kastik.apps.core.domain.usecases

import androidx.paging.PagingData
import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.model.aboard.AnnouncementPreview
import com.kastik.apps.core.model.aboard.AnnouncementView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPagedAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<PagingData<AnnouncementPreview>> {
        return userPreferencesRepository.getSortType()
            .flatMapLatest { sortType ->
                announcementRepository.getPagedAnnouncements(
                    sortType,
                    query = "",
                    authorIds = emptyList(),
                    tagIds = emptyList(),
                )
            }
    }
}

class GetPagedFilteredAnnouncementsUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(
        query: String,
        authorIds: List<Int>,
        tagIds: List<Int>
    ): Flow<PagingData<AnnouncementPreview>> =
        announcementRepository.getPagedFilteredAnnouncements(query, authorIds, tagIds)
        userPreferencesRepository.getSortType()
            .flatMapLatest { sortType ->
                announcementRepository.getPagedAnnouncements(sortType, query, authorIds, tagIds)
            }
}

}

class GetAnnouncementWithIdUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) {
    operator fun invoke(id: Int): Flow<AnnouncementView> =
        announcementRepository.getAnnouncementWithId(id)
}


class RefreshAnnouncementWithIdUseCase @Inject constructor(
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke(id: Int) =
        announcementRepository.refreshAnnouncementWithId(id)
}

class ShouldRefreshAnnouncementsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(): Flow<Unit> =
        profileRepository.isSignedIn()
            .distinctUntilChanged()
            .drop(1)
            .map { }
}