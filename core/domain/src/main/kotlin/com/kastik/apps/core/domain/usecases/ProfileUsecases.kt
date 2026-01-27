package com.kastik.apps.core.domain.usecases

import com.kastik.apps.core.domain.repository.AnnouncementRepository
import com.kastik.apps.core.domain.repository.AuthenticationRepository
import com.kastik.apps.core.domain.repository.ProfileRepository
import com.kastik.apps.core.model.aboard.UserData
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.concurrent.ExecutionException
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    operator fun invoke(): Flow<UserData> =
        combine(
            profileRepository.getProfile(),
            profileRepository.getEmailSubscriptions().map { it.toImmutableList() }
        ) { profile, subscriptions ->
            UserData(
                profile = profile,
                subscriptions = subscriptions
            )
        }
}

class RefreshUserProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() {
        try {
            profileRepository.refreshProfile()
        } catch (e: Exception) {
            //firebaseRepo.unsubscribeFromAllTopics()
            throw e
        }
    }
}

class RefreshSubscriptionsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke() {
        try {
            profileRepository.refreshEmailSubscriptions()
        } catch (e: Exception) {
            //firebaseRepo.unsubscribeFromAllTopics()
            throw e
        }
    }
}


class SubscribeToTagsUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(tags: List<Int>) {
        profileRepository.subscribeToEmailTags(tags)
        profileRepository.subscribeToTopics(tags)
    }
}

class SignOutUserUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val announcementRepository: AnnouncementRepository,
    private val authenticationRepository: AuthenticationRepository,

    ) {
    suspend operator fun invoke() {
        profileRepository.clearLocalData()
        authenticationRepository.clearAuthenticationData()
        announcementRepository.clearAnnouncementCache()
        try {
            profileRepository.unsubscribeFromAllTopics()
        } catch (e: ExecutionException) {
        }
    }
}