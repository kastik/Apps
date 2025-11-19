package com.kastik.apps.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.kastik.apps.core.datastore.AuthenticationLocalDataSource
import com.kastik.apps.core.datastore.AuthenticationLocalDataSourceImpl
import com.kastik.apps.core.datastore.UserPreferencesLocalDataSource
import com.kastik.apps.core.datastore.UserPreferencesLocalDataSourceImpl
import com.kastik.apps.core.datastore.proto.UserPreferences
import com.kastik.apps.core.datastore.serializers.UserPreferencesSerializer
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
    @AuthDatastore
    fun provideAuthPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("auth_prefs") }
    )

    @Provides
    @Singleton
    @UserPrefsDatastore
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile("user_prefs.pb") }
        )

    @Provides
    @Singleton
    fun provideAuthenticationLocalDataSource(
        @AuthDatastore dataStore: DataStore<Preferences>
    ): AuthenticationLocalDataSource = AuthenticationLocalDataSourceImpl(dataStore)

    @Provides
    @Singleton
    fun provideUserPreferencesLocalDataSource(
        @UserPrefsDatastore dataStore: DataStore<UserPreferences>
    ): UserPreferencesLocalDataSource = UserPreferencesLocalDataSourceImpl(dataStore)

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