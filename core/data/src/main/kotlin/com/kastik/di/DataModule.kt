package com.kastik.di

import android.content.Context
import androidx.room.Room
import com.kastik.data.repository.AnnouncementRepoImpl
import com.kastik.data.repository.AuthenticationRepositoryImpl
import com.kastik.data.repository.UserPreferencesRepoImpl
import com.kastik.database.db.AppDatabase
import com.kastik.datastore.AuthenticationLocalDataSource
import com.kastik.datastore.UserPreferencesLocalDataSource
import com.kastik.network.api.AboardApiClient
import com.kastik.network.datasource.AnnouncementRemoteDataSource
import com.kastik.network.datasource.AnnouncementRemoteDataSourceImpl
import com.kastik.network.datasource.AuthenticationRemoteDataSource
import com.kastik.repository.AnnouncementRepository
import com.kastik.repository.AuthenticationRepository
import com.kastik.repository.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java, "apps_database.db"
    ).fallbackToDestructiveMigration(true)
        .build()

    @Provides
    fun provideAnnouncementDao(db: AppDatabase) = db.announcementDao()


    @Provides
    @Singleton
    fun provideAnnouncementRemoteDataSource(
        api: AboardApiClient
    ): AnnouncementRemoteDataSource = AnnouncementRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideAnnouncementRepository(
        remoteDataSource: AnnouncementRemoteDataSource,
        database: AppDatabase,
        applicationContext: Application
    ): AnnouncementRepository = AnnouncementRepoImpl(remoteDataSource, database, applicationContext)


    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        local: AuthenticationLocalDataSource, remote: AuthenticationRemoteDataSource
    ): AuthenticationRepository = AuthenticationRepositoryImpl(local, remote)

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        datastore: UserPreferencesLocalDataSource
    ): UserPreferencesRepository = UserPreferencesRepoImpl(datastore)

}

