package com.kastik.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kastik.datastore.AuthenticationLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("auth_prefs") }
    )

    @Provides
    @Singleton
    fun provideAuthenticationLocalDataSource(
        dataStore: DataStore<Preferences>
    ): AuthenticationLocalDataSource = AuthenticationLocalDataSource(dataStore)

    @Provides
    @Singleton
    fun provideAppCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
}

@Singleton
class TokenProvider @Inject constructor(
    localDataSource: AuthenticationLocalDataSource,
    appScope: CoroutineScope
) {
    val token: StateFlow<String?> =
        localDataSource.getAboardAccessTokenFlow()
            .distinctUntilChanged()
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )

    fun getToken(): String? = token.value
}