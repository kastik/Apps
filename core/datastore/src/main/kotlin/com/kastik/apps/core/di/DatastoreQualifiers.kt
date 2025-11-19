package com.kastik.apps.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthDatastore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserPrefsDatastore