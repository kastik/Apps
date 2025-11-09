package com.kastik.di

import com.kastik.repository.AnnouncementRepository
import com.kastik.repository.AuthenticationRepository
import com.kastik.usecases.ExchangeCodeForAboardTokenUseCase
import com.kastik.usecases.ExchangeCodeForAppsTokenUseCase
import com.kastik.usecases.GetAnnouncementWithIdUseCase
import com.kastik.usecases.GetAuthorsUseCase
import com.kastik.usecases.GetPagedAnnouncementsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AnnouncementUseCaseModule {

    @Module
    @InstallIn(ViewModelComponent::class)
    object AnnouncementUseCaseModule {

        @Provides
        @ViewModelScoped
        fun provideGetPagedAnnouncementsUseCase(
            repo: AnnouncementRepository
        ): GetPagedAnnouncementsUseCase = GetPagedAnnouncementsUseCase(repo)
    }

    @Provides
    @ViewModelScoped
    fun provideGetAnnouncementWithIdUseCase(
        repo: AnnouncementRepository
    ): GetAnnouncementWithIdUseCase = GetAnnouncementWithIdUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideRequestAppsAccessTokenUseCase(
        repository: AuthenticationRepository
    ): ExchangeCodeForAppsTokenUseCase = ExchangeCodeForAppsTokenUseCase(repository)


    @Provides
    @ViewModelScoped
    fun provideRequestAboardAccessTokenUseCase(
        repository: AuthenticationRepository
    ): ExchangeCodeForAboardTokenUseCase = ExchangeCodeForAboardTokenUseCase(repository)


    @Provides
    @ViewModelScoped
    fun provideGetAuthorsUseCase(
        repository: AnnouncementRepository
    ): GetAuthorsUseCase = GetAuthorsUseCase(repository)
}