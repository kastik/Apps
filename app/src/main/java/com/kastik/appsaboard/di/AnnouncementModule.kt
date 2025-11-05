package com.kastik.appsaboard.di

import com.kastik.appsaboard.data.datasource.remote.api.AboardApiClient
import com.kastik.appsaboard.data.datasource.remote.source.AnnouncementRemoteDataSource
import com.kastik.appsaboard.data.repository.AnnouncementRepoImpl
import com.kastik.appsaboard.domain.repository.AnnouncementRepository
import com.kastik.appsaboard.domain.usecases.GetAnnouncementsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnnouncementNetworkModule {

    @Provides
    @Singleton
    fun provideAnnouncementApi(@AnnRetrofit retrofit: Retrofit): AboardApiClient =
        retrofit.create(AboardApiClient::class.java)

    @Provides
    @Singleton
    fun provideAnnouncementRemoteDataSource(
        api: AboardApiClient
    ): AnnouncementRemoteDataSource = AnnouncementRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideAnnouncementRepository(
        remoteDataSource: AnnouncementRemoteDataSource
    ): AnnouncementRepository = AnnouncementRepoImpl(remoteDataSource)
}

@Module
@InstallIn(ViewModelComponent::class)
object AnnouncementUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetPublicAnnouncementsUseCase(
        announcementRepository: AnnouncementRepository,
    ): GetAnnouncementsUseCase = GetAnnouncementsUseCase(announcementRepository)

}
